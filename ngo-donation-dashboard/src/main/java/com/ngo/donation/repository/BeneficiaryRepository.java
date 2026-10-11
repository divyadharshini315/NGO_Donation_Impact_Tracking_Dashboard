package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByStatus(String status);

    List<Beneficiary> findByNameContainingIgnoreCase(String name);
}
