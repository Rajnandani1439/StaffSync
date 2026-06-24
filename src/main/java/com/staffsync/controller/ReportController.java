package com.staffsync.controller;

import com.staffsync.model.Employee;
import com.staffsync.repository.AttendanceRepository;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.LeaveRequestRepository;
import com.staffsync.repository.PayrollRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRepository payrollRepository;

    public ReportController(EmployeeRepository employeeRepository,
                            AttendanceRepository attendanceRepository,
                            LeaveRequestRepository leaveRequestRepository,
                            PayrollRepository payrollRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.payrollRepository = payrollRepository;
    }

    @GetMapping
    public String reports(Model model) {
        model.addAttribute("currentPage", "reports");

        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus("ACTIVE");
        BigDecimal totalPayroll = payrollRepository.sumNetSalary();
        LocalDate today = LocalDate.now();
        long presentCount = attendanceRepository.countByDateAndStatus(today, "Present");
        long absentCount = attendanceRepository.countByDateAndStatus(today, "Absent");
        long lateCount = attendanceRepository.countByDateAndStatus(today, "Late");
        long pendingLeaves = leaveRequestRepository.countByStatus("PENDING");
        long approvedLeaves = leaveRequestRepository.countByStatus("APPROVED");
        long rejectedLeaves = leaveRequestRepository.countByStatus("REJECTED");
        long totalPayrollRecords = payrollRepository.count();

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("totalPayroll", totalPayroll);
        model.addAttribute("totalPayrollRecords", totalPayrollRecords);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        model.addAttribute("lateCount", lateCount);
        model.addAttribute("pendingLeaves", pendingLeaves);
        model.addAttribute("approvedLeaves", approvedLeaves);
        model.addAttribute("rejectedLeaves", rejectedLeaves);

        double avgAttendance = (presentCount + lateCount + absentCount) > 0
                ? (double) presentCount / (presentCount + lateCount + absentCount) * 100.0
                : 0.0;
        model.addAttribute("avgAttendance", String.format("%.1f", avgAttendance));

        List<Map<String, Object>> deptWise = new ArrayList<>();
        List<String> departments = employeeRepository.findDistinctDepartments();
        for (String dept : departments) {
            List<Employee> emps = employeeRepository.findByDepartment(dept);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("department", dept);
            row.put("total", emps.size());
            row.put("active", (int) emps.stream().filter(e -> "ACTIVE".equals(e.getStatus())).count());
            row.put("payroll", BigDecimal.ZERO);
            deptWise.add(row);
        }
        model.addAttribute("deptWise", deptWise);

        List<Map<String, Object>> attendanceSummary = new ArrayList<>();
        Map<String, Object> summaryRow = new LinkedHashMap<>();
        summaryRow.put("month", today.getMonth().toString().charAt(0) + today.getMonth().toString().substring(1).toLowerCase() + " " + today.getYear());
        summaryRow.put("avgPresent", String.format("%.1f", avgAttendance));
        double avgAbsent = (presentCount + lateCount + absentCount) > 0
                ? (double) absentCount / (presentCount + lateCount + absentCount) * 100.0
                : 0.0;
        double avgLate = (presentCount + lateCount + absentCount) > 0
                ? (double) lateCount / (presentCount + lateCount + absentCount) * 100.0
                : 0.0;
        summaryRow.put("avgLate", String.format("%.1f", avgLate));
        summaryRow.put("avgAbsent", String.format("%.1f", avgAbsent));
        attendanceSummary.add(summaryRow);
        model.addAttribute("attendanceSummary", attendanceSummary);

        model.addAttribute("monthlyPayroll", Collections.emptyList());
        return "report/reports";
    }
}
