package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.ImpactReport;

public interface ImpactReportRepository
        extends JpaRepository<ImpactReport, Long> {

    List<ImpactReport> findByCampaignId(Long campaignId);
}