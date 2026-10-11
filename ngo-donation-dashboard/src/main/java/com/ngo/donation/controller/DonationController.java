package com.ngo.donation.controller;

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

import com.ngo.donation.entity.Donation;
import com.ngo.donation.repository.DonationRepository;
import com.ngo.donation.service.DonationService;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationRepository donationRepository;
    private final DonationService donationService;

    public DonationController(
            DonationRepository donationRepository,
            DonationService donationService) {

        this.donationRepository = donationRepository;
        this.donationService = donationService;
    }

    // Create donation
    @PostMapping
    public ResponseEntity<Donation> createDonation(
            @RequestBody Donation donation) {

        Donation savedDonation =
                donationService.createDonation(donation);

        return ResponseEntity.ok(savedDonation);
    }

    // Get all donations
    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {

        return ResponseEntity.ok(
                donationRepository.findAll()
        );
    }

    // Get donation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(
            @PathVariable Long id) {

        return donationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get donations by donor email
    @GetMapping("/donor/{email}")
    public ResponseEntity<List<Donation>> getDonationsByDonor(
            @PathVariable String email) {

        return ResponseEntity.ok(
                donationRepository.findByDonorEmail(email)
        );
    }

    // Get donations by campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Donation>> getDonationsByCampaign(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                donationRepository.findByCampaignId(campaignId)
        );
    }

    // Update donation
    @PutMapping("/{id}")
    public ResponseEntity<Donation> updateDonation(
            @PathVariable Long id,
            @RequestBody Donation donation) {

        return donationRepository.findById(id)
                .map(existingDonation -> {

                    existingDonation.setDonorEmail(
                            donation.getDonorEmail());

                    existingDonation.setCampaignId(
                            donation.getCampaignId());

                    existingDonation.setAmount(
                            donation.getAmount());

                    existingDonation.setStatus(
                            donation.getStatus());

                    return ResponseEntity.ok(
                            donationRepository.save(existingDonation)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete donation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(
            @PathVariable Long id) {

        if (!donationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        donationRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}