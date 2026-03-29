package com.Food_Donation.dto;

import com.Food_Donation.enums.NgoStatus;
import com.Food_Donation.enums.NGORequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NGORegistrationDTO {

    private Long id;
    private Long userId;
    private String name;
    private String phone;
    private NGORequestType requesterType;
    private String address;
    private String city;
    private String pinCode;
    private String orgName;
    private String regNo;
    private NgoStatus status;
    private LocalDateTime created_at;
    private LocalDateTime approvedDate;
    private String documentKey;

}
