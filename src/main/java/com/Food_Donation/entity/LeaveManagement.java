package com.Food_Donation.entity;

import com.Food_Donation.enums.LeaveRequestStatus;
import com.Food_Donation.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

//@Getter
//@Setter
//@ToString
@Data
@Entity
public class LeaveManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long totalDays;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;


}
