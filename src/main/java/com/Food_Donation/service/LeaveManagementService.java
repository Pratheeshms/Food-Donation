package com.Food_Donation.service;

import com.Food_Donation.dto.LeaveManagementDTO;
import com.Food_Donation.entity.LeaveManagement;
import com.Food_Donation.enums.LeaveRequestStatus;
import com.Food_Donation.enums.Role;
import com.Food_Donation.exception.EmptyDataException;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.repository.LeaveManagementRepository;
import com.Food_Donation.utils.DataMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class LeaveManagementService {

    private final LeaveManagementRepository leaveManagementRepository;
    private final DataMapper dataMapper;

    public LeaveManagementDTO create(LeaveManagementDTO leaveManagementDTO, Long userId, String role) {

        if(leaveManagementDTO.getToDate().isBefore(leaveManagementDTO.getFromDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "To date must be after From date");
        }
        if (leaveManagementDTO.getToDate().isBefore(leaveManagementDTO.getFromDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"To date must be after from date");
        }

        LeaveManagement saved = dataMapper.DtoToModel(leaveManagementDTO);
        saved.setUserId(userId);
        saved.setRole(Role.valueOf(role));
        if (saved.getRole().equals("SUPER_ADMIN")){
            saved.setStatus(null);
        }else
        saved.setStatus(LeaveRequestStatus.PENDING);

        Long day = ChronoUnit.DAYS.between(
                saved.getFromDate().toLocalDate(),
                saved.getToDate().toLocalDate()
        )+1;
        saved.setTotalDays(day);

        LeaveManagement leaveManagement = leaveManagementRepository.save(saved);

        return dataMapper.ModelToDto(leaveManagement);

    }

    public LeaveManagementDTO findById(Long id) {

        LeaveManagement leaveManagement = leaveManagementRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Leave not found with id " + id));
        return dataMapper.ModelToDto(leaveManagement);
    }
    public List<LeaveManagementDTO> findAllApproved() {

        if (leaveManagementRepository.findAll().isEmpty()){
            throw new EmptyDataException("No leave record found");
        }
        List<LeaveManagementDTO> leave = leaveManagementRepository.findAllByStatusOrRole(LeaveRequestStatus.APPROVED_BY_ADMIN, Role.ADMIN)
                .stream()
                .map(dataMapper::ModelToDto)
                .toList();
        return leave;
    }

    public List<LeaveManagementDTO> findAllPending() {

            List<LeaveManagement> list =
                    leaveManagementRepository.findAllByStatus(LeaveRequestStatus.PENDING);

            if (list.isEmpty()) {
                throw new EmptyDataException("No leave record found");
            }
            return list.stream()
                    .map(dataMapper::ModelToDto)
                    .toList();
    }
}
