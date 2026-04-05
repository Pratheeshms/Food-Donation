package com.Food_Donation.repository;

import com.Food_Donation.entity.AvailableFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableFoodRepository extends JpaRepository<AvailableFood, Long> {

    boolean existsByFoodName(String foodName);
}
