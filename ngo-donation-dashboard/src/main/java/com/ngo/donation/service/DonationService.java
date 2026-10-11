
package com.ngo.donation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngo.donation.entity.Campaign;
import com.ngo.donation.entity.Donation;
import com.ngo.donation.repository.CampaignRepository;
import com.ngo.donation.repository.DonationRepository;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;

    public DonationService(
            DonationRepository donationRepository,
            CampaignRepository campaignRepository) {

        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
    }

    // Create donation and update campaign collected amount
    @Transactional
    public Donation createDonation(Donation donation) {

        // Find campaign first
        Campaign campaign = campaignRepository
                .findById(donation.getCampaignId())
                .orElseThrow(() ->
                        new RuntimeException("Campaign not found"));

        // Reject donations if campaign is not active
        if (!"ACTIVE".equalsIgnoreCase(campaign.getStatus())) {
            throw new RuntimeException(
                    "This campaign is not active. Donations are closed.");
        }

        // Get current collected amount
        Double currentAmount = campaign.getCollectedAmount();

        if (currentAmount == null) {
            currentAmount = 0.0;
        }

        // Add donation amount
        Double newAmount = currentAmount + donation.getAmount();

        campaign.setCollectedAmount(newAmount);

        // Automatically complete campaign when target is reached
        if (newAmount >= campaign.getTargetAmount()) {
            campaign.setStatus("COMPLETED");
        }

        // Save updated campaign and donation together
        campaignRepository.save(campaign);

        return donationRepository.save(donation);
    }
}
