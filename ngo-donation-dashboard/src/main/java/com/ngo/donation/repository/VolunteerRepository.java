
package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.Volunteer;

public interface VolunteerRepository
        extends JpaRepository<Volunteer, Long> {

    Volunteer findByEmail(String email);

    List<Volunteer> findByStatus(String status);

    List<Volunteer> findByNameContainingIgnoreCase(String name);

    List<Volunteer> findByAvailability(String availability);
}
