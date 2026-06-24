package com.staffsync.repository;

import com.staffsync.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    long countByStatus(String status);

    @Query("SELECT DISTINCT e.department FROM Employee e WHERE e.department IS NOT NULL")
    List<String> findDistinctDepartments();

    List<Employee> findByDepartment(String department);
    List<Employee> findTop5ByOrderByIdDesc();
}
