
package com.ngo.donation.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.donation.dto.LoginRequestDTO;
import com.ngo.donation.dto.RegisterRequestDTO;
import com.ngo.donation.dto.UserResponseDTO;
import com.ngo.donation.entity.User;
import com.ngo.donation.entity.Volunteer;
import com.ngo.donation.repository.VolunteerRepository;
import com.ngo.donation.service.OtpService;
import com.ngo.donation.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    private final OtpService otpService;

    private final VolunteerRepository volunteerRepository;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthController(
            UserService userService,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
            OtpService otpService,
            VolunteerRepository volunteerRepository) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.volunteerRepository = volunteerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        User user = userService.getUserByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            role = "DONOR";
        }

        // Volunteer approval check
        if ("VOLUNTEER".equalsIgnoreCase(role)) {

            if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
                return ResponseEntity.status(403)
                        .body("Your volunteer account is pending Admin verification.");
            }

            Volunteer volunteer =
                    volunteerRepository.findByEmail(user.getEmail());

            if (volunteer == null
                    || !"ACTIVE".equalsIgnoreCase(volunteer.getStatus())) {

                return ResponseEntity.status(403)
                        .body("Your volunteer account is pending Admin verification.");
            }
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        userService.loadUserByUsername(
                                user.getEmail()).getAuthorities()
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context, httpRequest, httpResponse);

        return ResponseEntity.ok(
                new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        role
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDTO request) {

        String requestedRole = request.getRole();

        if (requestedRole == null || requestedRole.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Invalid role");
        }

        requestedRole = requestedRole.trim().toUpperCase();

        Set<String> allowedRoles = Set.of(
                "DONOR",
                "VOLUNTEER",
                "HELP_SEEKER",
                "ADMIN"
        );

        if (!allowedRoles.contains(requestedRole)) {
            return ResponseEntity.badRequest()
                    .body("Invalid role");
        }

        // Allow only one Admin account
        if ("ADMIN".equals(requestedRole)) {

            if (userService.adminExists()) {
                return ResponseEntity.badRequest()
                        .body("Admin account already exists");
            }

            String expectedCode = System.getenv("NGO_ADMIN_SECRET");
            String suppliedCode = request.getAdminSecretCode();

            if (expectedCode == null || expectedCode.isBlank()
                    || suppliedCode == null
                    || !expectedCode.equals(suppliedCode)) {

                return ResponseEntity.badRequest()
                        .body("Invalid Admin secret code");
            }
        }

        // Verify OTP
        boolean verified = otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body("Invalid or expired OTP");
        }

        // Prevent duplicate email registration
        if (userService.getUserByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                requestedRole
        );

        // New volunteers must wait for Admin approval
        if ("VOLUNTEER".equals(requestedRole)) {
            user.setStatus("PENDING_VERIFICATION");
        }

        User savedUser = userService.saveUser(user);

        // Save volunteer details with matching status
        if ("VOLUNTEER".equals(requestedRole)) {

            Volunteer volunteer = new Volunteer(
                    request.getName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getAddress(),
                    request.getSkills(),
                    request.getAvailability(),
                    "PENDING_VERIFICATION"
            );

            volunteerRepository.save(volunteer);
        }

        return ResponseEntity.ok(
                new UserResponseDTO(
                        savedUser.getId(),
                        savedUser.getName(),
                        savedUser.getEmail(),
                        savedUser.getRole()
                )
        );
    }
}
