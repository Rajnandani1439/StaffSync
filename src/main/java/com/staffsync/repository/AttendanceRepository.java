package com.staffsync.repository;

import com.staffsync.model.Attendance;
import com.staffsync.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDateOrderByEmployeeAsc(LocalDate date);
    List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId);
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    long countByDateAndStatus(LocalDate date, String status);
    long countByEmployeeIdAndDateBetweenAndStatus(Long employeeId, LocalDate start, LocalDate end, String status);
    long countByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);
}
