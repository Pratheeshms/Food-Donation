package com.Food_Donation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class AvailableFoodPatchDTO extends AbstractDTO{


    private Long id;
    private Boolean isActive;
    private BigDecimal quantity;
    private LocalDateTime expiryTime;

}
