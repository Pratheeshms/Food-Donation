package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.LeaveManagementDTO;
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

    @PostMapping("/create")
    public ResponseEntity<LeaveManagementDTO>leave (@RequestBody LeaveManagementDTO leaveManagementDTO, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        String role = securityConfig.extractRole(token);
        LeaveManagementDTO leaveManagement = leaveManagementService.create(leaveManagementDTO, userId, role);

        return ResponseEntity.ok().body(leaveManagement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveManagementDTO>leave (@PathVariable Long id){

        LeaveManagementDTO leaveManagement = leaveManagementService.findById(id);
        return ResponseEntity.ok().body(leaveManagement);
    }

    @GetMapping("")
    public ResponseEntity<List<LeaveManagementDTO>>leave (){

        List<LeaveManagementDTO> leaveManagement = leaveManagementService.findAll();
        return ResponseEntity.ok().body(leaveManagement);
    }



}
