package com.staffsync.controller;

import com.staffsync.model.Employee;
import com.staffsync.model.LeaveRequest;
import com.staffsync.model.Payroll;
import com.staffsync.repository.AttendanceRepository;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.LeaveRequestRepository;
import com.staffsync.repository.PayrollRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRepository payrollRepository;

    public DashboardController(EmployeeRepository employeeRepository,
                               AttendanceRepository attendanceRepository,
                               LeaveRequestRepository leaveRequestRepository,
                               PayrollRepository payrollRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.payrollRepository = payrollRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now();
        long totalEmployees = employeeRepository.count();
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("presentToday", attendanceRepository.countByDateAndStatus(today, "Present"));
        model.addAttribute("pendingLeaves", leaveRequestRepository.countByStatus("PENDING"));
        model.addAttribute("onLeave", leaveRequestRepository.countByStatus("APPROVED"));
        model.addAttribute("activeEmployees", employeeRepository.countByStatus("ACTIVE"));

        List<Map<String, Object>> recentActivities = new ArrayList<>();
        for (Employee emp : employeeRepository.findTop5ByOrderByIdDesc()) {
            recentActivities.add(Map.of("user", emp.getFirstName() + " " + emp.getLastName(),
                    "action", "Employee added", "time", "Recently", "type", "profile"));
        }
        for (LeaveRequest lr : leaveRequestRepository.findTop5ByOrderByAppliedOnDesc()) {
            recentActivities.add(Map.of("user", lr.getEmployee().getFirstName() + " " + lr.getEmployee().getLastName(),
                    "action", "Submitted " + lr.getLeaveType() + " leave", "time", "Recently", "type", "leave"));
        }
        for (Payroll pr : payrollRepository.findTop5ByOrderByPayDateDesc()) {
            recentActivities.add(Map.of("user", pr.getEmployee().getFirstName() + " " + pr.getEmployee().getLastName(),
                    "action", "Payroll generated for " + pr.getPayPeriod(), "time", "Recently", "type", "payroll"));
        }
        if (recentActivities.size() > 5) {
            recentActivities = recentActivities.subList(0, 5);
        }
        model.addAttribute("recentActivities", recentActivities);

        List<Map<String, Object>> departmentStats = new ArrayList<>();
        List<String> departments = employeeRepository.findDistinctDepartments();
        for (String dept : departments) {
            List<Employee> emps = employeeRepository.findByDepartment(dept);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("name", dept);
            row.put("count", emps.size());
            row.put("present", 0);
            departmentStats.add(row);
        }
        model.addAttribute("departmentStats", departmentStats);

        model.addAttribute("currentPage", "dashboard");
        return "dashboard/dashboard";
    }
}
