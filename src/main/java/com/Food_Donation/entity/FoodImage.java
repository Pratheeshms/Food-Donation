package com.Food_Donation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FoodImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageKey;
    private String imageName;
    private String imageType;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private AvailableFood availableFood;
}
