package com.staffsync.repository;

import com.staffsync.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findAllByOrderByStartDateDesc();
    List<LeaveRequest> findByEmployeeIdOrderByStartDateDesc(Long employeeId);
}
