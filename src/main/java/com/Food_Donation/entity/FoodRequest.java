package com.Food_Donation.entity;

import com.Food_Donation.enums.DeliveryType;
import com.Food_Donation.enums.FoodRequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class FoodRequest extends AbstractEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private FoodRequestStatus status;
    private DeliveryType deliveryType;
    private LocalDateTime approvalTime;
    private LocalDateTime pickupTime; //time set by ngo guy
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private AvailableFood food;

    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private NGORegistration ngoRegistration;

}
