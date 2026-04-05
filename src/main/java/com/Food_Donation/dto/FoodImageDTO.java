package com.Food_Donation.dto;

import com.Food_Donation.entity.FoodRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodImageDTO {

    private Long id;
    private String imageUrl;
    private String imageKey;
    private String imageName;
    private String imageType;
    private Long foodId;

}
