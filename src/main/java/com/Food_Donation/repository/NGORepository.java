package com.Food_Donation.repository;

import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NGORepository extends JpaRepository<NGORegistration, Long> {

    boolean existsByOrgName(String orgName);

    Optional<NGORegistration> findByUserId(Long userId);
}
