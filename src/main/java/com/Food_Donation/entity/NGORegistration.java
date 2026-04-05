package com.Food_Donation.entity;

import com.Food_Donation.enums.EscalatedTo;
import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.enums.NGORequestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class NGORegistration extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String phone;
    @Enumerated(EnumType.STRING)
    private NGORequestType requesterType;
    private String address;
    private String city;
    private String pinCode;
    private String orgName;
    private String regNo;
    @Enumerated(EnumType.STRING)
    private NgoStatus status;
//    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private EscalatedTo escalatedTo;
    private LocalDateTime approvedDate;
    private String documentKey;

}
