
package com.ngo.donation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngo.donation.entity.User;
import com.ngo.donation.entity.Volunteer;
import com.ngo.donation.repository.UserRepository;
import com.ngo.donation.repository.VolunteerRepository;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;

    public VolunteerController(
            VolunteerRepository volunteerRepository,
            UserRepository userRepository) {
        this.volunteerRepository = volunteerRepository;
        this.userRepository = userRepository;
    }

    // Get all volunteers
    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerRepository.findAll());
    }

    // Get volunteer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Volunteer> getVolunteerById(
            @PathVariable Long id) {
        return volunteerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get volunteers by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Volunteer>> getByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                volunteerRepository.findByStatus(status));
    }

    // Search volunteer by name
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Volunteer>> searchByName(
            @PathVariable String name) {
        return ResponseEntity.ok(
                volunteerRepository.findByNameContainingIgnoreCase(name));
    }

    // Get volunteers by availability
    @GetMapping("/availability/{availability}")
    public ResponseEntity<List<Volunteer>> getByAvailability(
            @PathVariable String availability) {
        return ResponseEntity.ok(
                volunteerRepository.findByAvailability(availability));
    }

    // Admin: Approve volunteer
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveVolunteer(@PathVariable Long id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElse(null);

        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findByEmail(volunteer.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("Volunteer user account not found.");
        }

        volunteer.setStatus("ACTIVE");
        user.setStatus("ACTIVE");

        volunteerRepository.save(volunteer);
        userRepository.save(user);

        return ResponseEntity.ok("Volunteer approved successfully.");
    }

    // Admin: Reject volunteer
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectVolunteer(@PathVariable Long id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElse(null);

        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findByEmail(volunteer.getEmail());

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("Volunteer user account not found.");
        }

        volunteer.setStatus("REJECTED");
        user.setStatus("REJECTED");

        volunteerRepository.save(volunteer);
        userRepository.save(user);

        return ResponseEntity.ok("Volunteer rejected.");
    }

    // Admin: Delete volunteer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        if (!volunteerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        volunteerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
