package com.Food_Donation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveManagementDTO {

    private Long id;
    private Long userId;
    private String name;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long totalDays;
    private String role;


}
