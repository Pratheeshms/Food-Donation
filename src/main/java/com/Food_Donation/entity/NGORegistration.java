package com.Food_Donation.entity;

import com.Food_Donation.enums.EscalatedTo;
import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.enums.RequestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class NGORegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private RequestType requesterType;
    private String address;
    private String city;
    private String pinCode;
    private String orgName;
    private String regNo;
    @Enumerated(EnumType.STRING)
    private NgoStatus status;
    private LocalDateTime created_at;
    @Enumerated(EnumType.STRING)
    private EscalatedTo escalatedTo;

}
