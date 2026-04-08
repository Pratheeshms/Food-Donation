package com.Food_Donation.dto;

import com.Food_Donation.enums.DeliveryType;
import com.Food_Donation.enums.FoodRequestStatus;
import com.Food_Donation.enums.FoodStatus;
import com.Food_Donation.enums.FoodType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FoodRequestDTO {

    private Long id;
    private String foodName;
    private Long foodId;
    private FoodType foodType;
    private BigDecimal quantity;
    private FoodRequestStatus status;
    private DeliveryType deliveryType;
    private LocalDateTime approvalTime;
    private LocalDateTime pickupTime;
    private String remarks;
    private Long restaurantId;

}
