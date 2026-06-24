package com.staffsync.repository;

import com.staffsync.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findAllByOrderByPayDateDesc();
    List<Payroll> findByEmployeeIdOrderByPayDateDesc(Long employeeId);
}
