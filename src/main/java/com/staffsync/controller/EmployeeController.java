package com.staffsync.controller;

import com.staffsync.model.Employee;
import com.staffsync.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("currentPage", "employees");
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("currentPage", "employees");
        model.addAttribute("employee", new Employee());
        return "employee/add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Employee employee) {
        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("Active");
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "employees");
        Optional<Employee> emp = employeeRepository.findById(id);
        model.addAttribute("employee", emp.orElse(new Employee()));
        return "employee/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Employee employee) {
        employee.setId(id);
        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("Active");
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "employees");
        Optional<Employee> emp = employeeRepository.findById(id);
        model.addAttribute("employee", emp.orElse(new Employee()));
        return "employee/view";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/employees";
    }
}
