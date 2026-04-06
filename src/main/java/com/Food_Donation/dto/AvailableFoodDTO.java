package com.Food_Donation.dto;

import com.Food_Donation.enums.FoodStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableFoodDTO extends AbstractDTO {

    private Long id;
    private String foodName;
    private String foodType;
    private BigDecimal quantity;
    private String description;
//    private LocalDateTime cookedTime;
@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime expiryTime;
    private FoodStatus status;
    private Long restaurantId;


    private List<FoodImageDTO> foodImageDTOList;
}
