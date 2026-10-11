package com.ngo.donation.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.donation.entity.FundUtilization;
import com.ngo.donation.repository.FundUtilizationRepository;

@RestController
@RequestMapping("/api/fund-utilization")
public class FundUtilizationController {

    private final FundUtilizationRepository fundUtilizationRepository;

    public FundUtilizationController(
            FundUtilizationRepository fundUtilizationRepository) {

        this.fundUtilizationRepository = fundUtilizationRepository;
    }

    // Create fund utilization
    @PostMapping
    public ResponseEntity<FundUtilization> createFundUtilization(
            @RequestBody FundUtilization fundUtilization) {

        if (fundUtilization.getUtilizationDate() == null) {
            fundUtilization.setUtilizationDate(LocalDateTime.now());
        }

        FundUtilization saved =
                fundUtilizationRepository.save(fundUtilization);

        return ResponseEntity.ok(saved);
    }

    // Get all fund utilization records
    @GetMapping
    public ResponseEntity<List<FundUtilization>> getAllFundUtilization() {

        return ResponseEntity.ok(
                fundUtilizationRepository.findAll()
        );
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FundUtilization> getById(
            @PathVariable Long id) {

        return fundUtilizationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get utilization by campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<FundUtilization>> getByCampaign(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                fundUtilizationRepository
                        .findByCampaignId(campaignId)
        );
    }

    // Update fund utilization
    @PutMapping("/{id}")
    public ResponseEntity<FundUtilization> updateFundUtilization(
            @PathVariable Long id,
            @RequestBody FundUtilization fundUtilization) {

        return fundUtilizationRepository.findById(id)
                .map(existing -> {

                    existing.setCampaignId(
                            fundUtilization.getCampaignId());

                    existing.setAmount(
                            fundUtilization.getAmount());

                    existing.setPurpose(
                            fundUtilization.getPurpose());

                    existing.setDescription(
                            fundUtilization.getDescription());

                    existing.setUtilizationDate(
                            fundUtilization.getUtilizationDate());

                    return ResponseEntity.ok(
                            fundUtilizationRepository.save(existing)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete fund utilization
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFundUtilization(
            @PathVariable Long id) {

        if (!fundUtilizationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        fundUtilizationRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
