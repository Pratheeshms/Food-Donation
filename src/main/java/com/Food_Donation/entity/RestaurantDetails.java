package com.Food_Donation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class RestaurantDetails extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String pinCode;
    private String licenseNumber;

    @OneToMany(mappedBy = "restaurantDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailableFood> availableFoodList;

//    @OneToMany(mappedBy = "restaurant_id")
//    private List<AvailableFood> availableFoodList;
}
