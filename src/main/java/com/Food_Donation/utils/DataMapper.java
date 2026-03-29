package com.Food_Donation.utils;

import com.Food_Donation.dto.LeaveManagementDTO;
import com.Food_Donation.dto.NGORegistrationDTO;
import com.Food_Donation.entity.LeaveManagement;
import com.Food_Donation.entity.NGORegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Configuration;

@Mapper(componentModel = "spring")
public interface DataMapper {

    NGORegistration DtoToModel(NGORegistrationDTO ngoRegistrationDTO);
    NGORegistrationDTO ModelToDto(NGORegistration ngoRegistration);

    LeaveManagement DtoToModel(LeaveManagementDTO leaveManagementDTO);

    @Mapping(source = "userDetail.userName", target = "name")
    LeaveManagementDTO ModelToDto(LeaveManagement leaveManagement);
}
