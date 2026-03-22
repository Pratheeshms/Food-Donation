package com.Food_Donation.entity;

import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.enums.RequestType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class NGORegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private RequestType requesterType;
    private String address;
    private String city;
    private String pinCode;
    private String orgName;
    private String regNo;
    private NgoStatus status;
    private LocalDateTime created_at;
}
