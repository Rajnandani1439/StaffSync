package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("currentPage", "attendance");
        List<Map<String, Object>> records = new ArrayList<>();
        records.add(Map.of("id", 1, "employee", "Sarah Johnson", "date", "2026-06-23", "checkIn", "08:55", "checkOut", "17:10", "status", "Present"));
        records.add(Map.of("id", 2, "employee", "Michael Chen", "date", "2026-06-23", "checkIn", "09:00", "checkOut", "17:05", "status", "Present"));
        records.add(Map.of("id", 3, "employee", "Emily Rodriguez", "date", "2026-06-23", "checkIn", "08:45", "checkOut", "17:15", "status", "Present"));
        records.add(Map.of("id", 4, "employee", "David Kim", "date", "2026-06-23", "checkIn", "09:30", "checkOut", "17:00", "status", "Late"));
        records.add(Map.of("id", 5, "employee", "Lisa Thompson", "date", "2026-06-23", "checkIn", "08:50", "checkOut", "17:20", "status", "Present"));
        records.add(Map.of("id", 6, "employee", "James Wilson", "date", "2026-06-23", "checkIn", "-", "checkOut", "-", "status", "Absent"));
        records.add(Map.of("id", 7, "employee", "Anna Martinez", "date", "2026-06-23", "checkIn", "09:05", "checkOut", "13:00", "status", "Half Day"));
        records.add(Map.of("id", 8, "employee", "Robert Brown", "date", "2026-06-22", "checkIn", "08:55", "checkOut", "17:10", "status", "Present"));
        records.add(Map.of("id", 9, "employee", "Jennifer Davis", "date", "2026-06-22", "checkIn", "09:15", "checkOut", "18:00", "status", "Late"));
        records.add(Map.of("id", 10, "employee", "Maria Garcia", "date", "2026-06-22", "checkIn", "08:50", "checkOut", "17:05", "status", "Present"));
        model.addAttribute("records", records);
        return "attendance/list";
    }

    @GetMapping("/mark")
    public String markForm(Model model) {
        model.addAttribute("currentPage", "attendance");
        model.addAttribute("today", LocalDate.now().toString());
        return "attendance/mark";
    }

    @GetMapping("/report")
    public String report(Model model) {
        model.addAttribute("currentPage", "attendance");
        List<Map<String, Object>> reportData = new ArrayList<>();
        reportData.add(Map.of("employee", "Sarah Johnson", "department", "Engineering", "present", 21, "absent", 1, "late", 0, "halfDay", 0, "total", 22));
        reportData.add(Map.of("employee", "Michael Chen", "department", "Marketing", "present", 19, "absent", 1, "late", 2, "halfDay", 0, "total", 22));
        reportData.add(Map.of("employee", "Emily Rodriguez", "department", "Finance", "present", 20, "absent", 1, "late", 0, "halfDay", 1, "total", 22));
        reportData.add(Map.of("employee", "David Kim", "department", "Engineering", "present", 18, "absent", 0, "late", 3, "halfDay", 1, "total", 22));
        reportData.add(Map.of("employee", "Lisa Thompson", "department", "HR", "present", 22, "absent", 0, "late", 0, "halfDay", 0, "total", 22));
        reportData.add(Map.of("employee", "James Wilson", "department", "Operations", "present", 17, "absent", 3, "late", 1, "halfDay", 0, "total", 21));
        model.addAttribute("report", reportData);
        return "attendance/report";
    }
}
