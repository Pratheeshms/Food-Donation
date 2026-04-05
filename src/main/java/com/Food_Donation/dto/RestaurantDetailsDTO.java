package com.Food_Donation.dto;

import lombok.Data;

@Data
public class RestaurantDetailsDTO extends AbstractDTO{

    private Long id;

    private String restaurantName;
    private String ownerName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String pinCode;
    private String licenseNumber;

}
