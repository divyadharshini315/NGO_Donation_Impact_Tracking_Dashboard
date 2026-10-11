
package com.ngo.donation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.donation.entity.HelpRequest;
import com.ngo.donation.service.HelpRequestService;

@RestController
@RequestMapping("/api/help-requests")
@CrossOrigin
public class HelpRequestController {

    private final HelpRequestService service;

    public HelpRequestController(HelpRequestService service) {
        this.service = service;
    }

    // Submit a new help request
    @PostMapping
    public HelpRequest submitRequest(@RequestBody HelpRequest request) {
        return service.submitRequest(request);
    }

    // Get all requests
    @GetMapping
    public List<HelpRequest> getAllRequests() {
        return service.getAllRequests();
    }

    // Get requests for a help seeker by email
    @GetMapping("/my")
    public List<HelpRequest> getMyRequests(@RequestParam String email) {
        return service.getRequestsByEmail(email);
    }

    // Get requests by status
    @GetMapping("/status/{status}")
    public List<HelpRequest> getRequestsByStatus(
            @PathVariable String status) {
        return service.getRequestsByStatus(status);
    }

    // Volunteer verifies a request
    @PutMapping("/{id}/verify")
    public HelpRequest verifyRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return service.verifyRequest(id, body.get("notes"));
    }

    // Admin approves a verified request
    @PutMapping("/{id}/approve")
    public HelpRequest approveRequest(@PathVariable Long id) {
        return service.approveRequest(id);
    }

    // Admin rejects a request
    @PutMapping("/{id}/reject")
    public HelpRequest rejectRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return service.rejectRequest(id, body.get("reason"));
    }
}