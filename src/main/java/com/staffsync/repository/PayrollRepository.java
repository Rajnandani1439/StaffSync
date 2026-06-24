package com.staffsync.repository;

import com.staffsync.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findAllByOrderByPayDateDesc();
    List<Payroll> findByEmployeeIdOrderByPayDateDesc(Long employeeId);

    @Query("SELECT COALESCE(SUM(p.netSalary), 0) FROM Payroll p")
    BigDecimal sumNetSalary();

    List<Payroll> findTop5ByOrderByPayDateDesc();
}
