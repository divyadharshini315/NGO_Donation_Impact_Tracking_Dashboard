
package com.ngo.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.HelpRequest;

public interface HelpRequestRepository
        extends JpaRepository<HelpRequest, Long> {

    List<HelpRequest> findByEmail(String email);

    List<HelpRequest> findByStatus(String status);
}