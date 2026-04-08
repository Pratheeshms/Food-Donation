package com.Food_Donation.repository;

import com.Food_Donation.entity.FoodImage;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {

//    List<FoodImage> findByFoodId(List<Long> foodId);

    List<FoodImage> findByAvailableFood_IdIn(List<Long> foodId);


//    List<FoodImage> findByFoodId(Long id);
}
