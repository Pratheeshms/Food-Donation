package com.Food_Donation.dto;

import com.Food_Donation.entity.UserLogin;
import com.Food_Donation.enums.LeaveRequestStatus;
import com.Food_Donation.enums.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Role role;
    private LeaveRequestStatus status;
    private LocalDateTime approvedDate;


}
