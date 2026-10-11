
package com.ngo.donation.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngo.donation.entity.Otp;
import com.ngo.donation.repository.OtpRepository;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(
            OtpRepository otpRepository,
            JavaMailSender mailSender) {

        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
    }

    // ==========================================
    // GENERATE AND SEND OTP
    // ==========================================

    @Transactional
    public String generateOtp(String email) {

        // Delete old OTP for this email
        otpRepository.deleteByEmail(email);

        // Generate a secure 6-digit OTP
        String otpCode = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        // OTP expires after 5 minutes
        Otp otp = new Otp(
                email,
                otpCode,
                LocalDateTime.now().plusMinutes(5)
        );

        // Save new OTP
        otpRepository.save(otp);

        // Send OTP through email
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "NGO Donation Dashboard - Email Verification OTP"
        );
        message.setText(
                "Your OTP for NGO Donation Dashboard registration is: "
                + otpCode
                + "\n\nThis OTP is valid for 5 minutes."
        );

        mailSender.send(message);

        return otpCode;
    }

    // ==========================================
    // VERIFY OTP
    // ==========================================

    @Transactional
    public boolean verifyOtp(
            String email,
            String enteredOtp) {

        Otp otp = otpRepository.findByEmail(email);

        // OTP not found
        if (otp == null) {
            return false;
        }

        // OTP expired
        if (LocalDateTime.now().isAfter(otp.getExpiryTime())) {
            otpRepository.delete(otp);
            return false;
        }

        // OTP does not match
        if (!otp.getOtp().equals(enteredOtp)) {
            return false;
        }

        // OTP verified successfully
        otpRepository.delete(otp);

        return true;
    }
}
