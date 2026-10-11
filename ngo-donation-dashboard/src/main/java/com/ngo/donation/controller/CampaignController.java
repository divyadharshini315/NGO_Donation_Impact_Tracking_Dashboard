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

import com.ngo.donation.entity.Campaign;
import com.ngo.donation.service.CampaignService;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(
            CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<Campaign> createCampaign(
            @RequestBody Campaign campaign) {

        return ResponseEntity.ok(
                campaignService.createCampaign(campaign)
        );
    }

    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns() {

        return ResponseEntity.ok(
                campaignService.getAllCampaigns()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(
            @PathVariable Long id) {

        Campaign campaign =
                campaignService.getCampaignById(id);

        if (campaign == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campaign);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Campaign>> getByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                campaignService.getCampaignsByStatus(status)
        );
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<Campaign>> searchByTitle(
            @PathVariable String title) {

        return ResponseEntity.ok(
                campaignService.searchCampaigns(title)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(
            @PathVariable Long id,
            @RequestBody Campaign campaign) {

        Campaign updated =
                campaignService.updateCampaign(id, campaign);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(
            @PathVariable Long id) {

        if (!campaignService.deleteCampaign(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}