package com.Food_Donation.utils;

import com.Food_Donation.dto.*;
import com.Food_Donation.entity.*;
import org.mapstruct.*;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataMapper {

    NGORegistration DtoToModel(NGORegistrationDTO ngoRegistrationDTO);
    NGORegistrationDTO ModelToDto(NGORegistration ngoRegistration);

    LeaveManagement DtoToModel(LeaveManagementDTO leaveManagementDTO);

    @Mapping(source = "userDetail.userName", target = "name")
    LeaveManagementDTO ModelToDto(LeaveManagement leaveManagement);


    UserLogin DtoToModel(UserLoginDTO userLoginDTO);
    UserLoginDTO ModelToDto(UserLogin userLogin);

//    @Mapping(source = "restaurantId", target = "restaurantDetails.id")
    AvailableFood DtoToModel(AvailableFoodDTO availableFoodDTO);

    @Mapping(source = "restaurantDetails.id", target = "restaurantId")
    @Mapping(source = "foodImageList", target = "foodImageDTOList")
    AvailableFoodDTO ModelToDto(AvailableFood availableFood);

//    @Mapping(target = "availableFood", ignore = true)
    @Mapping(source = "availableFood.id", target = "foodId")
    FoodImageDTO foodImageToDto(FoodImage foodImage);

    List<FoodImageDTO> foodImageListToDto(List<FoodImage> foodImageList);

    RestaurantDetails DtoToModel(RestaurantDetailsDTO restaurantDetailsDTO);
    RestaurantDetailsDTO ModelToDto(RestaurantDetails restaurantDetails);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           //mentioning the id means, hibernate won't create new only update
    @Mapping(target = "createdAt", ignore = true)    //these are used to not allow  to edit
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "foodImageList", ignore = true)
    void updateFoodFromDto(AvailableFoodDTO dto, @MappingTarget AvailableFood entity);

//    void updateFoodFromDto(AvailableFoodDTO dto, AvailableFood food);
}
