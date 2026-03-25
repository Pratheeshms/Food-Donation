package com.Food_Donation.entity;

import com.Food_Donation.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String role;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;


}
