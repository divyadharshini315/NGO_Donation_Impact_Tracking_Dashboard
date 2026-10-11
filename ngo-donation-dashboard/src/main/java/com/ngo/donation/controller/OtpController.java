package com.ngo.donation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.donation.service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    // Send OTP
    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {

        otpService.generateOtp(email);

        return "OTP sent successfully";
    }

    // Verify OTP
    @PostMapping("/verify")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean verified = otpService.verifyOtp(email, otp);

        if (verified) {
            return "OTP verified successfully";
        }

        return "Invalid or expired OTP";
    }
}