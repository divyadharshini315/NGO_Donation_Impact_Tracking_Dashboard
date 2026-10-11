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

import com.ngo.donation.entity.ImpactReport;
import com.ngo.donation.repository.ImpactReportRepository;

@RestController
@RequestMapping("/api/impact-reports")
public class ImpactReportController {

    private final ImpactReportRepository impactReportRepository;

    public ImpactReportController(
            ImpactReportRepository impactReportRepository) {

        this.impactReportRepository = impactReportRepository;
    }

    // Create impact report
    @PostMapping
    public ResponseEntity<ImpactReport> createImpactReport(
            @RequestBody ImpactReport impactReport) {

        if (impactReport.getReportDate() == null) {
            impactReport.setReportDate(LocalDateTime.now());
        }

        ImpactReport savedReport =
                impactReportRepository.save(impactReport);

        return ResponseEntity.ok(savedReport);
    }

    // Get all impact reports
    @GetMapping
    public ResponseEntity<List<ImpactReport>> getAllImpactReports() {

        return ResponseEntity.ok(
                impactReportRepository.findAll()
        );
    }

    // Get impact report by ID
    @GetMapping("/{id}")
    public ResponseEntity<ImpactReport> getImpactReportById(
            @PathVariable Long id) {

        return impactReportRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get reports by campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<ImpactReport>> getByCampaign(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                impactReportRepository
                        .findByCampaignId(campaignId)
        );
    }

    // Update impact report
    @PutMapping("/{id}")
    public ResponseEntity<ImpactReport> updateImpactReport(
            @PathVariable Long id,
            @RequestBody ImpactReport impactReport) {

        return impactReportRepository.findById(id)
                .map(existingReport -> {

                    existingReport.setCampaignId(
                            impactReport.getCampaignId());

                    existingReport.setTitle(
                            impactReport.getTitle());

                    existingReport.setSummary(
                            impactReport.getSummary());

                    existingReport.setBeneficiariesReached(
                            impactReport.getBeneficiariesReached());

                    existingReport.setActivities(
                            impactReport.getActivities());

                    existingReport.setReportDate(
                            impactReport.getReportDate());

                    return ResponseEntity.ok(
                            impactReportRepository.save(
                                    existingReport)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete impact report
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImpactReport(
            @PathVariable Long id) {

        if (!impactReportRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        impactReportRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}