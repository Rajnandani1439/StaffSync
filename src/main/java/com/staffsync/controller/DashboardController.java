package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEmployees", 248);
        model.addAttribute("presentToday", 196);
        model.addAttribute("pendingLeaves", 12);
        model.addAttribute("onLeave", 18);
        model.addAttribute("activeEmployees", 235);

        List<Map<String, Object>> recentActivities = new ArrayList<>();
        recentActivities.add(Map.of("user", "Sarah Johnson", "action", "Submitted leave request", "time", "10 minutes ago", "type", "leave"));
        recentActivities.add(Map.of("user", "Michael Chen", "action", "Marked attendance", "time", "30 minutes ago", "type", "attendance"));
        recentActivities.add(Map.of("user", "Emily Rodriguez", "action", "Updated profile", "time", "1 hour ago", "type", "profile"));
        recentActivities.add(Map.of("user", "David Kim", "action", "Payroll generated for June", "time", "2 hours ago", "type", "payroll"));
        recentActivities.add(Map.of("user", "Lisa Thompson", "action", "Approved leave request", "time", "3 hours ago", "type", "approve"));
        model.addAttribute("recentActivities", recentActivities);

        List<Map<String, Object>> departmentStats = new ArrayList<>();
        departmentStats.add(Map.of("name", "Engineering", "count", 78, "present", 65));
        departmentStats.add(Map.of("name", "Marketing", "count", 45, "present", 38));
        departmentStats.add(Map.of("name", "Finance", "count", 32, "present", 28));
        departmentStats.add(Map.of("name", "Human Resources", "count", 28, "present", 24));
        departmentStats.add(Map.of("name", "Operations", "count", 65, "present", 52));
        model.addAttribute("departmentStats", departmentStats);

        model.addAttribute("currentPage", "dashboard");
        return "dashboard/dashboard";
    }
}
