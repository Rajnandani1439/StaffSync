package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("currentPage", "leave");
        List<Map<String, Object>> leaves = new ArrayList<>();
        leaves.add(Map.of("id", 1, "employee", "Anna Martinez", "type", "Sick", "from", "2026-06-20", "to", "2026-06-24", "reason", "Medical appointment and recovery", "status", "Pending", "applied", "2026-06-18"));
        leaves.add(Map.of("id", 2, "employee", "Robert Brown", "type", "Vacation", "from", "2026-07-01", "to", "2026-07-05", "reason", "Family vacation", "status", "Approved", "applied", "2026-06-10"));
        leaves.add(Map.of("id", 3, "employee", "Jennifer Davis", "type", "Casual", "from", "2026-06-25", "to", "2026-06-25", "reason", "Personal errand", "status", "Pending", "applied", "2026-06-20"));
        leaves.add(Map.of("id", 4, "employee", "Thomas Anderson", "type", "Sick", "from", "2026-06-15", "to", "2026-06-16", "reason", "Flu recovery", "status", "Rejected", "applied", "2026-06-14"));
        leaves.add(Map.of("id", 5, "employee", "Kevin Lee", "type", "Vacation", "from", "2026-06-28", "to", "2026-07-02", "reason", "Traveling abroad", "status", "Approved", "applied", "2026-06-01"));
        leaves.add(Map.of("id", 6, "employee", "Maria Garcia", "type", "Casual", "from", "2026-06-22", "to", "2026-06-22", "reason", "Half day - personal", "status", "Approved", "applied", "2026-06-19"));
        model.addAttribute("leaves", leaves);
        return "leave/list";
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        model.addAttribute("currentPage", "leave");
        return "leave/apply";
    }
}
