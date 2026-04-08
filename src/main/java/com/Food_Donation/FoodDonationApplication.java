package com.Food_Donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class FoodDonationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDonationApplication.class, args);
		System.out.println("----------------started---------------------");
	}

}
