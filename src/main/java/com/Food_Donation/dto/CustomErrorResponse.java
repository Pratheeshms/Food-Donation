package com.Food_Donation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class CustomErrorResponse {

    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String error;
    private String message;


}
