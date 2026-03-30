package com.Food_Donation.repository;

import com.Food_Donation.entity.RefreshToken;
import com.Food_Donation.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByToken(String token);

    void deleteByUserLogin(UserLogin userLogin);}
