package com.staffsync.controller;

import com.staffsync.model.Attendance;
import com.staffsync.model.Employee;
import com.staffsync.model.User;
import com.staffsync.repository.AttendanceRepository;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public AttendanceController(AttendanceRepository attendanceRepository,
                                EmployeeRepository employeeRepository,
                                UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("currentPage", "attendance");
        LocalDate today = LocalDate.now();
        List<Attendance> records;

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            records = attendanceRepository.findByDateOrderByEmployeeAsc(today);
        } else {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                Employee emp = employeeRepository.findByUserId(user.getId()).orElse(null);
                if (emp != null) {
                    records = attendanceRepository.findByEmployeeIdOrderByDateDesc(emp.getId());
                    model.addAttribute("records", records);
                    model.addAttribute("today", today);
                    return "attendance/list";
                }
            }
            records = Collections.emptyList();
        }

        model.addAttribute("records", records);
        model.addAttribute("today", today);
        model.addAttribute("presentCount", attendanceRepository.countByDateAndStatus(today, "Present"));
        model.addAttribute("lateCount", attendanceRepository.countByDateAndStatus(today, "Late"));
        model.addAttribute("absentCount", attendanceRepository.countByDateAndStatus(today, "Absent"));
        model.addAttribute("halfDayCount", attendanceRepository.countByDateAndStatus(today, "Half Day"));
        return "attendance/list";
    }

    @GetMapping("/mark")
    public String markForm(Model model) {
        model.addAttribute("currentPage", "attendance");
        Attendance attendance = new Attendance();
        attendance.setDate(LocalDate.now());
        model.addAttribute("attendance", attendance);
        model.addAttribute("employees", employeeRepository.findAll());
        return "attendance/mark";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Attendance attendance,
                       @RequestParam Long employeeId,
                       RedirectAttributes redirect) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            redirect.addFlashAttribute("errorMessage", "Employee not found");
            return "redirect:/attendance/mark";
        }
        attendance.setEmployee(employee);
        Optional<Attendance> existing = attendanceRepository.findByEmployeeIdAndDate(
                employeeId, attendance.getDate());
        if (existing.isPresent()) {
            redirect.addFlashAttribute("errorMessage", "Attendance already recorded for this employee on " + attendance.getDate());
            return "redirect:/attendance/mark";
        }
        attendanceRepository.save(attendance);
        redirect.addFlashAttribute("successMessage", "Attendance marked successfully");
        return "redirect:/attendance";
    }

    @GetMapping("/report")
    public String report(Model model) {
        model.addAttribute("currentPage", "attendance");
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        List<Map<String, Object>> reportData = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();

        for (Employee emp : employees) {
            long present = attendanceRepository.countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), start, end, "Present");
            long absent = attendanceRepository.countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), start, end, "Absent");
            long late = attendanceRepository.countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), start, end, "Late");
            long halfDay = attendanceRepository.countByEmployeeIdAndDateBetweenAndStatus(emp.getId(), start, end, "Half Day");
            long total = present + absent + late + halfDay;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("employee", emp.getFirstName() + " " + emp.getLastName());
            row.put("department", emp.getDepartment());
            row.put("present", present);
            row.put("absent", absent);
            row.put("late", late);
            row.put("halfDay", halfDay);
            row.put("total", total);
            reportData.add(row);
        }

        model.addAttribute("report", reportData);
        model.addAttribute("month", currentMonth);
        return "attendance/report";
    }
}
