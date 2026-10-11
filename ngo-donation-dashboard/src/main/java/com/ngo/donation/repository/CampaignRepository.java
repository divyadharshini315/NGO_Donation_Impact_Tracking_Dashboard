package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByStatus(String status);

    List<Campaign> findByTitleContainingIgnoreCase(String title);
}