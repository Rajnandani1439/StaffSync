package com.staffsync.config;

import com.staffsync.model.*;
import com.staffsync.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRepository payrollRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      EmployeeRepository employeeRepository,
                      AttendanceRepository attendanceRepository,
                      LeaveRequestRepository leaveRequestRepository,
                      PayrollRepository payrollRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.payrollRepository = payrollRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN", "Administrator", null));
            roleRepository.save(new Role(null, "EMPLOYEE", "Employee", null));
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
        Role empRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("EMPLOYEE role not found"));

        // --- ADMIN ---
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("admin@staffsync.com");
            adminUser.setEnabled(true);
            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
        }
        if (employeeRepository.findByUserId(adminUser.getId()).isEmpty()) {
            Employee adminEmp = new Employee();
            adminEmp.setFirstName("Admin");
            adminEmp.setLastName("User");
            adminEmp.setEmail("admin@staffsync.com");
            adminEmp.setDepartment("Administration");
            adminEmp.setDesignation("System Administrator");
            adminEmp.setStatus("ACTIVE");
            adminEmp.setUser(adminUser);
            employeeRepository.save(adminEmp);
        }

        // --- ALICE (EMPLOYEE) ---
        User aliceUser = userRepository.findByUsername("alice").orElse(null);
        Employee aliceEmp;
        if (aliceUser == null) {
            aliceUser = new User();
            aliceUser.setUsername("alice");
            aliceUser.setPassword(passwordEncoder.encode("alice123"));
            aliceUser.setEmail("alice@staffsync.com");
            aliceUser.setEnabled(true);
            aliceUser.setRole(empRole);
            userRepository.save(aliceUser);
        }
        aliceEmp = employeeRepository.findByUserId(aliceUser.getId()).orElse(null);
        if (aliceEmp == null) {
            aliceEmp = new Employee();
            aliceEmp.setFirstName("Alice");
            aliceEmp.setLastName("Smith");
            aliceEmp.setEmail("alice@staffsync.com");
            aliceEmp.setPhone("9876543210");
            aliceEmp.setDepartment("Engineering");
            aliceEmp.setDesignation("Software Developer");
            aliceEmp.setHireDate(LocalDate.of(2025, 1, 15));
            aliceEmp.setSalary(BigDecimal.valueOf(75000));
            aliceEmp.setStatus("ACTIVE");
            aliceEmp.setUser(aliceUser);
            employeeRepository.save(aliceEmp);
        }

        // --- JOHN (EMPLOYEE) ---
        User johnUser = userRepository.findByUsername("john").orElse(null);
        Employee johnEmp;
        if (johnUser == null) {
            johnUser = new User();
            johnUser.setUsername("john");
            johnUser.setPassword(passwordEncoder.encode("john123"));
            johnUser.setEmail("john@staffsync.com");
            johnUser.setEnabled(true);
            johnUser.setRole(empRole);
            userRepository.save(johnUser);
        }
        johnEmp = employeeRepository.findByUserId(johnUser.getId()).orElse(null);
        if (johnEmp == null) {
            johnEmp = new Employee();
            johnEmp.setFirstName("John");
            johnEmp.setLastName("Doe");
            johnEmp.setEmail("john@staffsync.com");
            johnEmp.setPhone("9876543211");
            johnEmp.setDepartment("Marketing");
            johnEmp.setDesignation("Marketing Executive");
            johnEmp.setHireDate(LocalDate.of(2025, 3, 1));
            johnEmp.setSalary(BigDecimal.valueOf(65000));
            johnEmp.setStatus("ACTIVE");
            johnEmp.setUser(johnUser);
            employeeRepository.save(johnEmp);
        }

        // --- SAMPLE ATTENDANCE ---
        LocalDate today = LocalDate.now();
        if (attendanceRepository.findByEmployeeIdAndDate(aliceEmp.getId(), today).isEmpty()) {
            Attendance a1 = new Attendance();
            a1.setEmployee(aliceEmp);
            a1.setDate(today);
            a1.setCheckIn(LocalTime.of(9, 0));
            a1.setCheckOut(LocalTime.of(18, 0));
            a1.setStatus("Present");
            attendanceRepository.save(a1);
        }
        if (attendanceRepository.findByEmployeeIdAndDate(johnEmp.getId(), today).isEmpty()) {
            Attendance a2 = new Attendance();
            a2.setEmployee(johnEmp);
            a2.setDate(today);
            a2.setCheckIn(LocalTime.of(9, 30));
            a2.setCheckOut(LocalTime.of(18, 30));
            a2.setStatus("Late");
            attendanceRepository.save(a2);
        }
        if (attendanceRepository.findByEmployeeIdAndDate(aliceEmp.getId(), today.minusDays(1)).isEmpty()) {
            Attendance a3 = new Attendance();
            a3.setEmployee(aliceEmp);
            a3.setDate(today.minusDays(1));
            a3.setStatus("Present");
            attendanceRepository.save(a3);
        }
        if (attendanceRepository.findByEmployeeIdAndDate(johnEmp.getId(), today.minusDays(1)).isEmpty()) {
            Attendance a4 = new Attendance();
            a4.setEmployee(johnEmp);
            a4.setDate(today.minusDays(1));
            a4.setStatus("Absent");
            attendanceRepository.save(a4);
        }

        // --- SAMPLE LEAVE REQUESTS ---
        if (leaveRequestRepository.count() == 0) {
            LeaveRequest l1 = new LeaveRequest();
            l1.setEmployee(aliceEmp);
            l1.setStartDate(today.plusDays(5));
            l1.setEndDate(today.plusDays(7));
            l1.setLeaveType("Vacation");
            l1.setReason("Family trip");
            l1.setStatus("PENDING");
            l1.setAppliedOn(LocalDateTime.now().minusDays(1));
            leaveRequestRepository.save(l1);

            LeaveRequest l2 = new LeaveRequest();
            l2.setEmployee(johnEmp);
            l2.setStartDate(today.minusDays(10));
            l2.setEndDate(today.minusDays(8));
            l2.setLeaveType("Sick Leave");
            l2.setReason("Medical appointment");
            l2.setStatus("APPROVED");
            l2.setAppliedOn(LocalDateTime.now().minusDays(15));
            leaveRequestRepository.save(l2);

            LeaveRequest l3 = new LeaveRequest();
            l3.setEmployee(aliceEmp);
            l3.setStartDate(today.minusDays(20));
            l3.setEndDate(today.minusDays(19));
            l3.setLeaveType("Casual Leave");
            l3.setReason("Personal work");
            l3.setStatus("REJECTED");
            l3.setAppliedOn(LocalDateTime.now().minusDays(25));
            leaveRequestRepository.save(l3);
        }

        // --- SAMPLE PAYROLL RECORDS ---
        if (payrollRepository.count() == 0) {
            Payroll p1 = new Payroll();
            p1.setEmployee(aliceEmp);
            p1.setPayDate(today.withDayOfMonth(1));
            p1.setBasicSalary(BigDecimal.valueOf(75000));
            p1.setAllowances(BigDecimal.valueOf(5000));
            p1.setDeductions(BigDecimal.valueOf(2000));
            p1.setNetSalary(BigDecimal.valueOf(78000));
            p1.setPayPeriod(today.getMonthValue() + "-" + today.getYear());
            p1.setStatus("PAID");
            payrollRepository.save(p1);

            Payroll p2 = new Payroll();
            p2.setEmployee(johnEmp);
            p2.setPayDate(today.withDayOfMonth(1));
            p2.setBasicSalary(BigDecimal.valueOf(65000));
            p2.setAllowances(BigDecimal.valueOf(4000));
            p2.setDeductions(BigDecimal.valueOf(1500));
            p2.setNetSalary(BigDecimal.valueOf(67500));
            p2.setPayPeriod(today.getMonthValue() + "-" + today.getYear());
            p2.setStatus("PAID");
            payrollRepository.save(p2);
        }
    }
}
