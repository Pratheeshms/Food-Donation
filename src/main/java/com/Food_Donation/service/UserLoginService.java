package com.Food_Donation.service;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.UserLoginDTO;
import com.Food_Donation.entity.UserLogin;
import com.Food_Donation.exception.UnauthorizedException;
import com.Food_Donation.repository.UserLoginRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;

    public void create(UserLoginDTO userLoginDTO) {
        if (userLoginRepository.existsByUserName(userLoginDTO.getUserName()))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username already Exist");
        }
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(userLoginDTO.getUserName());
        userLogin.setRole("DONER");
        userLogin.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));

        userLoginRepository.save(userLogin);

    }

    public String login(UserLoginDTO userLoginDTO) {

        UserLogin userLogin = userLoginRepository.findByUserName(userLoginDTO.getUserName())

                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), userLogin.getPassword()))
                throw new UnauthorizedException("Invalid password");

        return securityConfig.generateToken(userLogin.getId(), userLogin.getPassword(), userLogin.getRole());




    }
}
