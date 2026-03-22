package com.Food_Donation.controller;

import com.Food_Donation.dto.LoginResponseDTO;
import com.Food_Donation.dto.UserLoginDTO;
import com.Food_Donation.service.UserLoginService;
import com.Food_Donation.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstant.BASE_PATH)
@RequiredArgsConstructor
public class UserLogin {

    private final UserLoginService userLoginService;

    @PostMapping("/sign-in")
    public ResponseEntity<String> userLogin(@RequestBody UserLoginDTO userLoginDTO)
    {
        userLoginService.create(userLoginDTO);
        return ResponseEntity.ok ("You are Successfully Login our Food Donation System");
    }

    @PostMapping("/login")
    public LoginResponseDTO userLogins(@RequestBody UserLoginDTO userLoginDTO)
    {
        String token = userLoginService.login(userLoginDTO);

        return new  LoginResponseDTO(token);
    }

}
