
package com.ngo.donation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ngo.donation.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByRole(String role);
}