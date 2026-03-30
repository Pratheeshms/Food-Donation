package com.Food_Donation.service;

import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.dto.S3FIleResponseDTO;
import com.Food_Donation.entity.LeaveManagement;
import com.Food_Donation.entity.NGORegistration;
import com.Food_Donation.enums.EscalatedTo;
import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.exception.DuplicateResourceException;
import com.Food_Donation.exception.EmptyDataException;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.repository.LeaveManagementRepository;
import com.Food_Donation.repository.NGORepository;
import com.Food_Donation.utils.DataMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NGOService {

    private final NGORepository ngoRepository;
    private final DataMapper dataMapper;
    private final LeaveManagementRepository leaveManagementRepository;
    private final S3Service s3Service;


    public NGORegistrationDTO create(NGORegistrationDTO ngoRegistrationDTO, MultipartFile document, Long userId) {


        LeaveManagement leaveManagement = new LeaveManagement();
        if (ngoRepository.existsByOrgName(ngoRegistrationDTO.getOrgName()))
        {
            throw new DuplicateResourceException("Org Name "+ ngoRegistrationDTO.getOrgName()+" already exist");
        }
        String key = s3Service.uploadFile(document);

        // Check SUPER_ADMIN on leave today
        LocalDate today = LocalDate.now();

        boolean superAdminOnLeave = leaveManagementRepository.findAll()
                .stream()
                .anyMatch(leave ->
                        leave.getRole().equals("SUPER-ADMIN") &&
//                                leave.getStatus().equals("APPROVED") &&
                                !today.isBefore(ChronoLocalDate.from(leave.getFromDate())) &&
                                !today.isAfter(ChronoLocalDate.from(leave.getToDate()))
                );

        NGORegistration ngoRegistration= dataMapper.DtoToModel(ngoRegistrationDTO);
        ngoRegistration.setStatus(NgoStatus.PENDING);
        ngoRegistration.setUserId(userId);
        ngoRegistration.setCreated_at(LocalDateTime.now());
        ngoRegistration.setDocumentKey(key);

        if (superAdminOnLeave) {
            ngoRegistration.setEscalatedTo(EscalatedTo.ADMIN);
        } else {
            ngoRegistration.setEscalatedTo(null);
        }

         NGORegistration savedNGO = ngoRepository.save(ngoRegistration);
         return dataMapper.ModelToDto(savedNGO);

    }

    public NGORegistration findById(Long userId) {

        return ngoRepository.findByUserId(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Profile not found. Please complete registration first.: "+userId));
    }

    public List<NGORegistration> findByRoles(Long userId, String role) {

        List<NGORegistration> ngoRegistration = new ArrayList<>();

        if(role.equals("SUPER_ADMIN")) {
            ngoRegistration = ngoRepository.findByStatusAndEscalatedToIsNull(NgoStatus.PENDING);
        } else if (role.equals("ADMIN")) {
            ngoRegistration = ngoRepository.findByStatusAndEscalatedTo(NgoStatus.PENDING, EscalatedTo.ADMIN);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

        if(ngoRegistration.isEmpty()) {
            throw new EmptyDataException( "No new request found");
        }
        return ngoRegistration;
    }

    public NGORegistrationDTO update(NGORegistrationDTO ngoRegistrationDTO) {

        NGORegistration ngo = ngoRepository.findById(ngoRegistrationDTO.getId())
                .orElseThrow(()-> new EmptyDataException("No new request arrived yet"));

        ngo.setStatus(ngoRegistrationDTO.getStatus());
        ngo.setApprovedDate(LocalDateTime.now());

        NGORegistration saved = ngoRepository.save(ngo);

        return dataMapper.ModelToDto(saved);

    }

    public S3FIleResponseDTO getDocumentKeys(Long id) {

        NGORegistration ngo = ngoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found"));

        String documentKey = ngo.getDocumentKey();
        String presignedUrl = s3Service.generatePresignedUrl(documentKey);
//        LocalDateTime expire = s3Service.
                LocalDateTime expiryTime = LocalDateTime.now()
                .plusMinutes(s3Service.getExpiration());

        S3FIleResponseDTO s3FIleResponseDTO = new S3FIleResponseDTO();
        s3FIleResponseDTO.setDocumentUrl(presignedUrl);
        s3FIleResponseDTO.setExpiryTime(expiryTime);

        return s3FIleResponseDTO;
    }
}
