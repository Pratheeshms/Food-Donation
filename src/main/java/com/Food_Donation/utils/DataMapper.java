package com.Food_Donation.utils;

import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.NGORegistration;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel = "spring")
public interface DataMapper {

    NGORegistration DtoToModel(NGORegistrationDTO ngoRegistrationDTO);

    NGORegistrationDTO ModelToDto(NGORegistration ngoRegistration);
}
