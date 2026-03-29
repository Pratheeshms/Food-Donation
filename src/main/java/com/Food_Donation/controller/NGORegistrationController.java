package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import com.Food_Donation.exception.ResourceNotFoundException;
import com.Food_Donation.exception.UnauthorizedException;
import com.Food_Donation.service.NGOService;
import com.Food_Donation.utils.AppConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH+"/registration")
public class NGORegistrationController {

    private final NGOService ngoService;
    private final SecurityConfig securityConfig;

    //while someone registration, if SUB ADMIN is on leave NGO request goes to ADMIN
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NGORegistrationDTO> nog(@RequestPart("ngo") String ngoReg, @RequestPart("document") MultipartFile document, HttpServletRequest request) throws JsonProcessingException {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);

        ObjectMapper mapper = new ObjectMapper();
        NGORegistrationDTO ngoRegistrationDTO =
                mapper.readValue(ngoReg, NGORegistrationDTO.class);

        NGORegistrationDTO saved = ngoService.create(ngoRegistrationDTO,document, userId);
        return ResponseEntity.ok().body(saved);
    }
    @GetMapping("/document/{id}")
    public ResponseEntity<String> getDocument(@PathVariable Long id) {

        String presignedUrl = ngoService.getDocumentKeys(id);

        return ResponseEntity.ok(presignedUrl);
    }
//    this API for registration guys to view their requested application
    @GetMapping("/user")
    public ResponseEntity<NGORegistration> ngoRegistration(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        NGORegistration ngoRegistration = ngoService.findById(userId);
        return ResponseEntity.ok().body(ngoRegistration);
    }

//    API to get all pending registration to ADMIN and Sub ADMIN
    @GetMapping("/admin")
    public ResponseEntity<List<NGORegistration>> responseEntity(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or Invalid Authorization Header");
        }
        String token = authHeader.substring(7);


        Long userId = securityConfig.extractUserId(token);
        String role = securityConfig.extractRole(token);
        List<NGORegistration> ngoRegistration = ngoService.findByRoles(userId,role);

        return ResponseEntity.ok().body(ngoRegistration);

    }

//    same API for both Admin and Super Admin
    @PutMapping("/update")
    public ResponseEntity<NGORegistrationDTO>updates(@RequestBody NGORegistrationDTO ngoRegistrationDTO){

        NGORegistrationDTO saved = ngoService.update(ngoRegistrationDTO);
        return ResponseEntity.ok().body(saved);
    }



}
