package com.ngo.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Otp findByEmail(String email);

    void deleteByEmail(String email);
}