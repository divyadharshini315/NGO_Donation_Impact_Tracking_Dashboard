
package com.ngo.donation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngo.donation.entity.HelpRequest;
import com.ngo.donation.repository.HelpRequestRepository;

@Service
public class HelpRequestService {

    private final HelpRequestRepository repository;

    public HelpRequestService(HelpRequestRepository repository) {
        this.repository = repository;
    }

    public HelpRequest submitRequest(HelpRequest request) {
        request.setId(null);
        request.setStatus("PENDING");
        request.setCreatedAt(java.time.LocalDateTime.now());

        return repository.save(request);
    }

    public List<HelpRequest> getAllRequests() {
        return repository.findAll();
    }

    public List<HelpRequest> getRequestsByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<HelpRequest> getRequestsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public HelpRequest getRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Help request not found"));
    }

    public HelpRequest verifyRequest(Long id, String notes) {
        HelpRequest request = getRequestById(id);

        request.setStatus("VERIFIED");
        request.setVerificationNotes(notes);

        return repository.save(request);
    }

    public HelpRequest approveRequest(Long id) {
        HelpRequest request = getRequestById(id);

        if (!"VERIFIED".equals(request.getStatus())) {
            throw new IllegalStateException(
                    "Only verified requests can be approved");
        }

        request.setStatus("APPROVED");
        request.setRejectionReason(null);

        return repository.save(request);
    }

    public HelpRequest rejectRequest(Long id, String reason) {
        HelpRequest request = getRequestById(id);

        request.setStatus("REJECTED");
        request.setRejectionReason(reason);

        return repository.save(request);
    }
}