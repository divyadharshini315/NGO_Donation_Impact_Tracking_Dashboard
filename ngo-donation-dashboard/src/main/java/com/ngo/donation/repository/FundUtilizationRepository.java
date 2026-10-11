package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.FundUtilization;

public interface FundUtilizationRepository
        extends JpaRepository<FundUtilization, Long> {

    List<FundUtilization> findByCampaignId(Long campaignId);
}
