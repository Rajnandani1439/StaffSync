package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private List<Map<String, Object>> getEmployees() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("id", 1, "firstName", "Sarah", "lastName", "Johnson", "email", "sarah.j@staffsync.com", "phone", "+1 555-0101", "department", "Engineering", "position", "Senior Developer", "salary", BigDecimal.valueOf(85000), "hireDate", "2022-03-15", "status", "Active"));
        list.add(Map.of("id", 2, "firstName", "Michael", "lastName", "Chen", "email", "michael.c@staffsync.com", "phone", "+1 555-0102", "department", "Marketing", "position", "Marketing Lead", "salary", BigDecimal.valueOf(72000), "hireDate", "2021-07-01", "status", "Active"));
        list.add(Map.of("id", 3, "firstName", "Emily", "lastName", "Rodriguez", "email", "emily.r@staffsync.com", "phone", "+1 555-0103", "department", "Finance", "position", "Financial Analyst", "salary", BigDecimal.valueOf(68000), "hireDate", "2023-01-10", "status", "Active"));
        list.add(Map.of("id", 4, "firstName", "David", "lastName", "Kim", "email", "david.k@staffsync.com", "phone", "+1 555-0104", "department", "Engineering", "position", "Backend Developer", "salary", BigDecimal.valueOf(78000), "hireDate", "2022-11-20", "status", "Active"));
        list.add(Map.of("id", 5, "firstName", "Lisa", "lastName", "Thompson", "email", "lisa.t@staffsync.com", "phone", "+1 555-0105", "department", "Human Resources", "position", "HR Manager", "salary", BigDecimal.valueOf(75000), "hireDate", "2020-05-18", "status", "Active"));
        list.add(Map.of("id", 6, "firstName", "James", "lastName", "Wilson", "email", "james.w@staffsync.com", "phone", "+1 555-0106", "department", "Operations", "position", "Operations Manager", "salary", BigDecimal.valueOf(82000), "hireDate", "2021-09-05", "status", "Active"));
        list.add(Map.of("id", 7, "firstName", "Anna", "lastName", "Martinez", "email", "anna.m@staffsync.com", "phone", "+1 555-0107", "department", "Engineering", "position", "Frontend Developer", "salary", BigDecimal.valueOf(76000), "hireDate", "2023-04-12", "status", "On Leave"));
        list.add(Map.of("id", 8, "firstName", "Robert", "lastName", "Brown", "email", "robert.b@staffsync.com", "phone", "+1 555-0108", "department", "Marketing", "position", "Content Writer", "salary", BigDecimal.valueOf(55000), "hireDate", "2023-02-28", "status", "Active"));
        list.add(Map.of("id", 9, "firstName", "Jennifer", "lastName", "Davis", "email", "jennifer.d@staffsync.com", "phone", "+1 555-0109", "department", "Finance", "position", "Accountant", "salary", BigDecimal.valueOf(62000), "hireDate", "2022-08-15", "status", "Active"));
        list.add(Map.of("id", 10, "firstName", "Thomas", "lastName", "Anderson", "email", "thomas.a@staffsync.com", "phone", "+1 555-0110", "department", "Operations", "position", "Logistics Coordinator", "salary", BigDecimal.valueOf(58000), "hireDate", "2023-06-01", "status", "Inactive"));
        list.add(Map.of("id", 11, "firstName", "Maria", "lastName", "Garcia", "email", "maria.g@staffsync.com", "phone", "+1 555-0111", "department", "Human Resources", "position", "Recruiter", "salary", BigDecimal.valueOf(54000), "hireDate", "2023-09-20", "status", "Active"));
        list.add(Map.of("id", 12, "firstName", "Kevin", "lastName", "Lee", "email", "kevin.l@staffsync.com", "phone", "+1 555-0112", "department", "Engineering", "position", "DevOps Engineer", "salary", BigDecimal.valueOf(90000), "hireDate", "2022-01-10", "status", "Active"));
        return list;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("currentPage", "employees");
        model.addAttribute("employees", getEmployees());
        return "employee/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("currentPage", "employees");
        return "employee/add";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("currentPage", "employees");
        Map<String, Object> emp = getEmployees().stream()
                .filter(e -> e.get("id").equals(id))
                .findFirst()
                .orElse(getEmployees().get(0));
        model.addAttribute("employee", emp);
        return "employee/edit";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("currentPage", "employees");
        Map<String, Object> emp = getEmployees().stream()
                .filter(e -> e.get("id").equals(id))
                .findFirst()
                .orElse(getEmployees().get(0));
        model.addAttribute("employee", emp);
        return "employee/view";
    }
}
