package com.Food_Donation.service;

import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.exception.DuplicateResourceException;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.repository.NGORepository;
import com.Food_Donation.utils.DataMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class NGOService {

    private final NGORepository ngoRepository;
    private final DataMapper dataMapper;

    public NGORegistrationDTO create(NGORegistrationDTO ngoRegistrationDTO, Long userId) {
        if (ngoRepository.existsByOrgName(ngoRegistrationDTO.getOrgName()))
        {
            throw new DuplicateResourceException("Org Name "+ ngoRegistrationDTO.getOrgName()+" already exist");
        }

        NGORegistration ngoRegistration= dataMapper.DtoToModel(ngoRegistrationDTO);
        ngoRegistration.setStatus(NgoStatus.PENDING);
        ngoRegistration.setUserId(userId);
        ngoRegistration.setCreated_at(LocalDateTime.now());

         NGORegistration savedNGO = ngoRepository.save(ngoRegistration);
         return dataMapper.ModelToDto(savedNGO);

    }

    public NGORegistration findById(Long userId) {

        return ngoRepository.findByUserId(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Profile not found. Please complete registration first.: "+userId));
    }
}
