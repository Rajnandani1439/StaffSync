package com.staffsync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/payroll")
public class PayrollController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("currentPage", "payroll");
        List<Map<String, Object>> payrolls = new ArrayList<>();
        payrolls.add(Map.of("id", 1, "employee", "Sarah Johnson", "department", "Engineering", "month", "June 2026", "basic", BigDecimal.valueOf(85000), "allowances", BigDecimal.valueOf(8500), "deductions", BigDecimal.valueOf(4250), "net", BigDecimal.valueOf(89250), "status", "Paid", "paidDate", "2026-06-25"));
        payrolls.add(Map.of("id", 2, "employee", "Michael Chen", "department", "Marketing", "month", "June 2026", "basic", BigDecimal.valueOf(72000), "allowances", BigDecimal.valueOf(7200), "deductions", BigDecimal.valueOf(3600), "net", BigDecimal.valueOf(75600), "status", "Paid", "paidDate", "2026-06-25"));
        payrolls.add(Map.of("id", 3, "employee", "Emily Rodriguez", "department", "Finance", "month", "June 2026", "basic", BigDecimal.valueOf(68000), "allowances", BigDecimal.valueOf(6800), "deductions", BigDecimal.valueOf(3400), "net", BigDecimal.valueOf(71400), "status", "Paid", "paidDate", "2026-06-24"));
        payrolls.add(Map.of("id", 4, "employee", "David Kim", "department", "Engineering", "month", "June 2026", "basic", BigDecimal.valueOf(78000), "allowances", BigDecimal.valueOf(7800), "deductions", BigDecimal.valueOf(3900), "net", BigDecimal.valueOf(81900), "status", "Pending", "paidDate", "-"));
        payrolls.add(Map.of("id", 5, "employee", "Lisa Thompson", "department", "HR", "month", "June 2026", "basic", BigDecimal.valueOf(75000), "allowances", BigDecimal.valueOf(7500), "deductions", BigDecimal.valueOf(3750), "net", BigDecimal.valueOf(78750), "status", "Paid", "paidDate", "2026-06-24"));
        payrolls.add(Map.of("id", 6, "employee", "James Wilson", "department", "Operations", "month", "June 2026", "basic", BigDecimal.valueOf(82000), "allowances", BigDecimal.valueOf(8200), "deductions", BigDecimal.valueOf(4100), "net", BigDecimal.valueOf(86100), "status", "Pending", "paidDate", "-"));
        model.addAttribute("payrolls", payrolls);
        return "payroll/list";
    }

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("currentPage", "payroll");
        return "payroll/generate";
    }

    @GetMapping("/payslip/{id}")
    public String payslip(@PathVariable Integer id, Model model) {
        model.addAttribute("currentPage", "payroll");
        Map<String, Object> payslip = new HashMap<>();
        payslip.put("id", 1);
        payslip.put("employee", "Sarah Johnson");
        payslip.put("department", "Engineering");
        payslip.put("designation", "Senior Developer");
        payslip.put("employeeId", "EMP-001");
        payslip.put("month", "June 2026");
        payslip.put("basic", BigDecimal.valueOf(85000));
        payslip.put("allowances", BigDecimal.valueOf(8500));
        payslip.put("deductions", BigDecimal.valueOf(4250));
        payslip.put("net", BigDecimal.valueOf(89250));
        payslip.put("status", "Paid");
        payslip.put("paidDate", "2026-06-25");
        payslip.put("bankName", "National Bank");
        payslip.put("accountNo", "XXXX-XXXX-1234");
        payslip.put("panNumber", "ABCDE1234F");
        payslip.put("workingDays", 22);
        payslip.put("paidDays", 22);
        model.addAttribute("payslip", payslip);
        return "payroll/payslip";
    }
}
