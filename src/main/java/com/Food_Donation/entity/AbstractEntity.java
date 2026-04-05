package com.Food_Donation.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractEntity {

    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

}
