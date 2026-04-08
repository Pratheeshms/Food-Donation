package com.Food_Donation.scheduler;

import com.Food_Donation.entity.AvailableFood;
import com.Food_Donation.enums.FoodStatus;
import com.Food_Donation.repository.AvailableFoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class FoodExpireMonitor {

    private final AvailableFoodRepository availableFoodRepository;

    @Scheduled(cron = "0 * * * * *") // every minute
    public void expire()
    {
        List<AvailableFood> availableFood = availableFoodRepository.findByActive(true);

        List<AvailableFood> updatedFoods = availableFood.stream()
                .filter(food ->
                        food.getExpiryTime() != null &&
                                food.getExpiryTime().isBefore(LocalDateTime.now())
                )
                .peek(food -> food.setStatus(FoodStatus.EXPIRED))
                .toList();

        availableFoodRepository.saveAll(updatedFoods);
    }

}
