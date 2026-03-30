package com.Food_Donation.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Data
public class S3FIleResponseDTO {

    private String documentUrl;
    private LocalDateTime expiryTime;

}
