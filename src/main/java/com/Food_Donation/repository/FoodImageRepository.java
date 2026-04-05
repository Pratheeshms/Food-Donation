package com.Food_Donation.repository;

import com.Food_Donation.entity.FoodImage;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodImageRepository extends JpaRepository<FoodImage, Long> {


//    List<FoodImage> findByFoodId(Long id);
}
