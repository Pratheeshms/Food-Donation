package com.Food_Donation.repository;

import com.Food_Donation.entity.LeaveManagement;
import com.Food_Donation.enums.LeaveRequestStatus;
import com.Food_Donation.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface LeaveManagementRepository extends JpaRepository<LeaveManagement, Long> {


    List<LeaveManagement> findAllByStatus(LeaveRequestStatus leaveRequestStatus);

//    List<LeaveManagement> findAllByStatusAndRole(LeaveRequestStatus leaveRequestStatus, Role role);

    List<LeaveManagement> findAllByStatusOrRole(LeaveRequestStatus leaveRequestStatus, Role role);
}
