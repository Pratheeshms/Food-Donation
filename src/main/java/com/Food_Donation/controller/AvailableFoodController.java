package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.AvailableFoodDTO;
import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.AvailableFood;
import com.Food_Donation.service.AvailableFoodService;
import com.Food_Donation.utils.AppConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(AppConstant.BASE_PATH+AppConstant.AVAILABLE_FOOD)
@AllArgsConstructor
public class AvailableFoodController {

    private final AvailableFoodService availableFoodService;
    private final SecurityConfig securityConfig;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvailableFoodDTO> food(@RequestPart("dto") String foodDto, @RequestPart("image") List<MultipartFile> imageDto,
                                                 HttpServletRequest request) throws JsonProcessingException
    {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        Long userId = securityConfig.extractUserId(token);
        AvailableFoodDTO availableFoodDTO =
                objectMapper.readValue(foodDto, AvailableFoodDTO.class);

        AvailableFoodDTO saved = availableFoodService.create(availableFoodDTO,imageDto, userId);
        return ResponseEntity.ok().body(saved);
    }

    @PutMapping("/update")
    public ResponseEntity<AvailableFoodDTO> foods(@RequestPart("dto") String foodDto, @RequestPart(value = "image", required = false)
    List<MultipartFile> imageDto) throws JsonProcessingException
    {
        AvailableFoodDTO availableFoodDTO = objectMapper.readValue(foodDto, AvailableFoodDTO.class);

        AvailableFoodDTO saved = availableFoodService.update(availableFoodDTO.getId(),availableFoodDTO, imageDto);
        return ResponseEntity.ok().body(saved);
    }

    @PatchMapping("/stock-status")
    public ResponseEntity<AvailableFoodDTO> stock(@RequestBody AvailableFoodDTO availableFoodDTO)
    {
        AvailableFoodDTO saved = availableFoodService.stockStatus(availableFoodDTO.getId(), availableFoodDTO);

        return ResponseEntity.ok().body(saved);
    }
}
