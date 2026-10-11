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

import com.ngo.donation.entity.Beneficiary;
import com.ngo.donation.repository.BeneficiaryRepository;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryController(
            BeneficiaryRepository beneficiaryRepository) {

        this.beneficiaryRepository = beneficiaryRepository;
    }

    // ==========================================
    // CREATE BENEFICIARY
    // ==========================================

    @PostMapping
    public ResponseEntity<Beneficiary> createBeneficiary(
            @RequestBody Beneficiary beneficiary) {

        Beneficiary savedBeneficiary =
                beneficiaryRepository.save(beneficiary);

        return ResponseEntity.ok(savedBeneficiary);
    }

    // ==========================================
    // GET ALL BENEFICIARIES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {

        return ResponseEntity.ok(
                beneficiaryRepository.findAll()
        );
    }

    // ==========================================
    // GET BENEFICIARY BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(
            @PathVariable Long id) {

        return beneficiaryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // GET BY STATUS
    // ==========================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Beneficiary>> getByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                beneficiaryRepository.findByStatus(status)
        );
    }

    // ==========================================
    // SEARCH BY NAME
    // ==========================================

    @GetMapping("/search/{name}")
    public ResponseEntity<List<Beneficiary>> searchByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                beneficiaryRepository
                        .findByNameContainingIgnoreCase(name)
        );
    }

    // ==========================================
    // UPDATE BENEFICIARY
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(
            @PathVariable Long id,
            @RequestBody Beneficiary beneficiary) {

        return beneficiaryRepository.findById(id)
                .map(existingBeneficiary -> {

                    existingBeneficiary.setName(
                            beneficiary.getName());

                    existingBeneficiary.setEmail(
                            beneficiary.getEmail());

                    existingBeneficiary.setPhone(
                            beneficiary.getPhone());

                    existingBeneficiary.setAddress(
                            beneficiary.getAddress());

                    existingBeneficiary.setNeedDescription(
                            beneficiary.getNeedDescription());

                    existingBeneficiary.setStatus(
                            beneficiary.getStatus());

                    // NEW
                    existingBeneficiary.setCampaignId(
                            beneficiary.getCampaignId());

                    return ResponseEntity.ok(
                            beneficiaryRepository.save(
                                    existingBeneficiary)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // DELETE BENEFICIARY
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(
            @PathVariable Long id) {

        if (!beneficiaryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        beneficiaryRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}