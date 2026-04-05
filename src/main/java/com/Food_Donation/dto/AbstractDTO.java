package com.Food_Donation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AbstractDTO {

    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
