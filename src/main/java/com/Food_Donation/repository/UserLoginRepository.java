package com.Food_Donation.repository;

import com.Food_Donation.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    boolean existsByUserName(String userName);

    Optional<UserLogin> findByUserName(String userName);

}
