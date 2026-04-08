package com.Food_Donation.entity;

import com.Food_Donation.enums.FoodStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class AvailableFood extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private String foodType;
    private BigDecimal quantity;
    private String description;
//    private LocalDateTime cookedTime;
    private LocalDateTime expiryTime;
    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantDetails restaurantDetails;

    @OneToMany(mappedBy = "availableFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodImage> foodImageList;


}
