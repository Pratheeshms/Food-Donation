package com.Food_Donation.repository;

import com.Food_Donation.entity.AvailableFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailableFoodRepository extends JpaRepository<AvailableFood, Long> {

    boolean existsByFoodName(String foodName);

    Page<AvailableFood> findAllByActive(boolean active, Pageable pageable);

    List<AvailableFood> findByActive(boolean b);
}
