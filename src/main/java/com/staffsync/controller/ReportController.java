package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @GetMapping
    public String reports(Model model) {
        model.addAttribute("currentPage", "reports");
        List<Map<String, Object>> deptWise = new ArrayList<>();
        deptWise.add(Map.of("department", "Engineering", "total", 78, "active", 76, "payroll", BigDecimal.valueOf(6120000)));
        deptWise.add(Map.of("department", "Marketing", "total", 45, "active", 43, "payroll", BigDecimal.valueOf(3240000)));
        deptWise.add(Map.of("department", "Finance", "total", 32, "active", 31, "payroll", BigDecimal.valueOf(1984000)));
        deptWise.add(Map.of("department", "Human Resources", "total", 28, "active", 28, "payroll", BigDecimal.valueOf(1624000)));
        deptWise.add(Map.of("department", "Operations", "total", 65, "active", 62, "payroll", BigDecimal.valueOf(3770000)));
        model.addAttribute("deptWise", deptWise);

        List<Map<String, Object>> monthlyPayroll = new ArrayList<>();
        monthlyPayroll.add(Map.of("month", "Jan 2026", "amount", BigDecimal.valueOf(16780000)));
        monthlyPayroll.add(Map.of("month", "Feb 2026", "amount", BigDecimal.valueOf(16540000)));
        monthlyPayroll.add(Map.of("month", "Mar 2026", "amount", BigDecimal.valueOf(16920000)));
        monthlyPayroll.add(Map.of("month", "Apr 2026", "amount", BigDecimal.valueOf(17100000)));
        monthlyPayroll.add(Map.of("month", "May 2026", "amount", BigDecimal.valueOf(16850000)));
        monthlyPayroll.add(Map.of("month", "Jun 2026", "amount", BigDecimal.valueOf(17080000)));
        model.addAttribute("monthlyPayroll", monthlyPayroll);

        List<Map<String, Object>> attendanceSummary = new ArrayList<>();
        attendanceSummary.add(Map.of("month", "Jan 2026", "avgPresent", 93.5, "avgLate", 4.2, "avgAbsent", 2.3));
        attendanceSummary.add(Map.of("month", "Feb 2026", "avgPresent", 91.8, "avgLate", 5.1, "avgAbsent", 3.1));
        attendanceSummary.add(Map.of("month", "Mar 2026", "avgPresent", 94.2, "avgLate", 3.8, "avgAbsent", 2.0));
        attendanceSummary.add(Map.of("month", "Apr 2026", "avgPresent", 92.1, "avgLate", 4.5, "avgAbsent", 3.4));
        attendanceSummary.add(Map.of("month", "May 2026", "avgPresent", 95.0, "avgLate", 3.2, "avgAbsent", 1.8));
        attendanceSummary.add(Map.of("month", "Jun 2026", "avgPresent", 93.0, "avgLate", 4.0, "avgAbsent", 3.0));
        model.addAttribute("attendanceSummary", attendanceSummary);

        model.addAttribute("totalEmployees", 248);
        model.addAttribute("activeEmployees", 235);
        model.addAttribute("monthlyPayrollTotal", BigDecimal.valueOf(17080000));
        model.addAttribute("avgAttendance", 93.2);
        return "report/reports";
    }
}
