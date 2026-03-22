package com.Food_Donation.controller;

import com.Food_Donation.service.NGOService;
import com.Food_Donation.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH+"/registration")
public class NGORegistration {

    private final NGOService ngoService;
    

}
