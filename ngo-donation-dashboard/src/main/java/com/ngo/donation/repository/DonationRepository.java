package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonorEmail(String donorEmail);

    List<Donation> findByCampaignId(Long campaignId);
}
