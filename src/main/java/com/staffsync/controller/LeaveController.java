package com.staffsync.controller;

import com.staffsync.model.Employee;
import com.staffsync.model.LeaveRequest;
import com.staffsync.model.User;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.LeaveRequestRepository;
import com.staffsync.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public LeaveController(LeaveRequestRepository leaveRequestRepository,
                           EmployeeRepository employeeRepository,
                           UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("currentPage", "leave");
        List<LeaveRequest> leaves;

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            leaves = leaveRequestRepository.findAllByOrderByStartDateDesc();
        } else {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                Employee emp = employeeRepository.findByUserId(user.getId()).orElse(null);
                if (emp != null) {
                    leaves = leaveRequestRepository.findByEmployeeIdOrderByStartDateDesc(emp.getId());
                } else {
                    leaves = List.of();
                }
            } else {
                leaves = List.of();
            }
        }

        model.addAttribute("leaves", leaves);
        return "leave/list";
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        model.addAttribute("currentPage", "leave");
        model.addAttribute("leaveRequest", new LeaveRequest());
        return "leave/apply";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute LeaveRequest leaveRequest,
                       Authentication auth,
                       RedirectAttributes redirect) {
        if (leaveRequest.getEndDate() != null && leaveRequest.getStartDate() != null
                && leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
            redirect.addFlashAttribute("errorMessage", "End date cannot be before start date.");
            return "redirect:/leave/apply";
        }

        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) {
            redirect.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/leave/apply";
        }
        Employee emp = employeeRepository.findByUserId(user.getId()).orElse(null);
        if (emp == null) {
            redirect.addFlashAttribute("errorMessage", "Employee profile not found. Contact admin.");
            return "redirect:/leave/apply";
        }

        leaveRequest.setEmployee(emp);
        leaveRequest.setStatus("PENDING");
        leaveRequest.setAppliedOn(LocalDateTime.now());
        leaveRequestRepository.save(leaveRequest);
        redirect.addFlashAttribute("successMessage", "Leave request submitted successfully.");
        return "redirect:/leave";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirect) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("APPROVED");
            leaveRequestRepository.save(leave);
            redirect.addFlashAttribute("successMessage", "Leave request approved.");
        }
        return "redirect:/leave";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes redirect) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("REJECTED");
            leaveRequestRepository.save(leave);
            redirect.addFlashAttribute("successMessage", "Leave request rejected.");
        }
        return "redirect:/leave";
    }
}
