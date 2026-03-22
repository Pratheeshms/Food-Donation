package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import com.Food_Donation.service.NGOService;
import com.Food_Donation.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH+"/registration")
public class NGORegistrationController {

    private final NGOService ngoService;
    private final SecurityConfig securityConfig;

    @PostMapping("/create")
    public ResponseEntity<NGORegistrationDTO> nog(@RequestBody NGORegistrationDTO ngoRegistrationDTO, HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        NGORegistrationDTO saved = ngoService.create(ngoRegistrationDTO, userId);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping("/user")
    public ResponseEntity<NGORegistration> ngoRegistration(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        NGORegistration ngoRegistration = ngoService.findById(userId);
        return ResponseEntity.ok().body(ngoRegistration);
    }

}
