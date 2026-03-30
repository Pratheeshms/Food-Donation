package com.Food_Donation.controller;

import com.Food_Donation.configuration.SecurityConfig;
import com.Food_Donation.dto.LoginResponseDTO;
import com.Food_Donation.entity.RefreshToken;
import com.Food_Donation.service.RefreshTokenService;
import com.Food_Donation.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstant.BASE_PATH+ AppConstant.REFRESH_TOKEN)
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final SecurityConfig securityConfig;

    @PostMapping("/refresh")
    public LoginResponseDTO refreshToken(@RequestParam String refreshToken) {

        RefreshToken token = refreshTokenService.verifyRefreshToken(refreshToken);

        String accessToken = securityConfig.generateToken(
                token.getUserLogin().getId(),
                token.getUserLogin().getUserName(),
                token.getUserLogin().getRole()
        );

        return new LoginResponseDTO(accessToken, refreshToken);
    }
    @PostMapping("/logout")
    public String logout(@RequestParam String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return "Logged out successfully";
    }


}
