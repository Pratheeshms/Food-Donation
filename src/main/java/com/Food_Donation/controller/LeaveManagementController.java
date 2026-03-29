package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.LeaveManagementDTO;
import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.LeaveManagement;
import com.Food_Donation.service.LeaveManagementService;
import com.Food_Donation.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstant.BASE_PATH+"/leave")
@RequiredArgsConstructor
public class LeaveManagementController {

    private final LeaveManagementService leaveManagementService;
    private final SecurityConfig securityConfig;

    //create leave request for all employees, admin and super admin
    @PostMapping("/create")
    public ResponseEntity<LeaveManagementDTO>leave (@RequestBody LeaveManagementDTO leaveManagementDTO, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        String role = securityConfig.extractRole(token);
        LeaveManagementDTO leaveManagement = leaveManagementService.create(leaveManagementDTO, userId, role);

        return ResponseEntity.ok().body(leaveManagement);
    }

    //just an ordinary findById
    @GetMapping("/{id}")
    public ResponseEntity<LeaveManagementDTO>leave (@PathVariable Long id){

        LeaveManagementDTO leaveManagement = leaveManagementService.findById(id);
        return ResponseEntity.ok().body(leaveManagement);
    }

    // just an find to fell all APPROVED data by  Super-ADMIN
    @GetMapping("")
    public ResponseEntity<List<LeaveManagementDTO>>leave (){

        List<LeaveManagementDTO> leaveManagement = leaveManagementService.findAllApproved();
        return ResponseEntity.ok().body(leaveManagement);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<LeaveManagementDTO>>PendingLeaves (){

        List<LeaveManagementDTO> leaveManagement = leaveManagementService.findAllPending();
        return ResponseEntity.ok().body(leaveManagement);
    }


    @PutMapping("/update")
    public ResponseEntity<LeaveManagementDTO>updates(@RequestBody LeaveManagementDTO leaveManagementDTO){

        LeaveManagementDTO saved = leaveManagementService.update(leaveManagementDTO);
        return ResponseEntity.ok().body(saved);
    }
}
