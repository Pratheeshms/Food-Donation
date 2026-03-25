package com.Food_Donation.repository;

import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import com.Food_Donation.enums.EscalatedTo;
import com.Food_Donation.enums.NgoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NGORepository extends JpaRepository<NGORegistration, Long> {

    boolean existsByOrgName(String orgName);

    Optional<NGORegistration> findByUserId(Long userId);

    List<NGORegistration> findByStatusAndEscalatedToIsNull(NgoStatus ngoStatus);

    List<NGORegistration> findByStatusAndEscalatedTo(NgoStatus ngoStatus, EscalatedTo escalatedTo);
}
