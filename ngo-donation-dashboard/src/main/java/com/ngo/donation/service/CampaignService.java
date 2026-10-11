
package com.ngo.donation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngo.donation.entity.Campaign;
import com.ngo.donation.repository.CampaignRepository;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    // Create a new campaign
    public Campaign createCampaign(Campaign campaign) {

        if (campaign.getCollectedAmount() == null) {
            campaign.setCollectedAmount(0.0);
        }

        if (campaign.getStatus() == null ||
                campaign.getStatus().isBlank()) {
            campaign.setStatus("ACTIVE");
        }

        // Automatically complete if target is already reached
        if (campaign.getTargetAmount() != null &&
                campaign.getCollectedAmount()
                        >= campaign.getTargetAmount()) {
            campaign.setStatus("COMPLETED");
        }

        return campaignRepository.save(campaign);
    }

    // Get all campaigns
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    // Get campaign by ID
    public Campaign getCampaignById(Long id) {
        return campaignRepository.findById(id).orElse(null);
    }

    // Get campaigns by status
    public List<Campaign> getCampaignsByStatus(String status) {
        return campaignRepository.findByStatus(status);
    }

    // Search campaigns by title
    public List<Campaign> searchCampaigns(String title) {
        return campaignRepository
                .findByTitleContainingIgnoreCase(title);
    }

    // Update campaign details and status
    public Campaign updateCampaign(
            Long id,
            Campaign campaign) {

        Campaign existing =
                campaignRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        if (campaign.getTitle() != null) {
            existing.setTitle(campaign.getTitle());
        }

        if (campaign.getDescription() != null) {
            existing.setDescription(campaign.getDescription());
        }

        if (campaign.getTargetAmount() != null) {
            existing.setTargetAmount(campaign.getTargetAmount());
        }

        // Allow Admin to update campaign status
        if (campaign.getStatus() != null &&
                !campaign.getStatus().isBlank()) {

            String newStatus =
                    campaign.getStatus().trim().toUpperCase();

            if (!newStatus.equals("ACTIVE") &&
                    !newStatus.equals("INACTIVE") &&
                    !newStatus.equals("COMPLETED")) {
                throw new IllegalArgumentException(
                        "Invalid campaign status");
            }

            existing.setStatus(newStatus);
        }

        // Do not update collected amount here.
        // DonationService manages the actual collected amount.

        // Automatically complete when target is reached
        if (existing.getTargetAmount() != null &&
                existing.getCollectedAmount() != null &&
                existing.getCollectedAmount()
                        >= existing.getTargetAmount()) {
            existing.setStatus("COMPLETED");
        }

        return campaignRepository.save(existing);
    }

    // Delete campaign
    public boolean deleteCampaign(Long id) {

        if (!campaignRepository.existsById(id)) {
            return false;
        }

        campaignRepository.deleteById(id);
        return true;
    }
}
