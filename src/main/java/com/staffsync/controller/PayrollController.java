package com.staffsync.controller;

import com.staffsync.model.Employee;
import com.staffsync.model.Payroll;
import com.staffsync.model.User;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.PayrollRepository;
import com.staffsync.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public PayrollController(PayrollRepository payrollRepository,
                             EmployeeRepository employeeRepository,
                             UserRepository userRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        model.addAttribute("currentPage", "payroll");
        List<Payroll> payrolls;

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            payrolls = payrollRepository.findAllByOrderByPayDateDesc();
        } else {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                Employee emp = employeeRepository.findByUserId(user.getId()).orElse(null);
                if (emp != null) {
                    payrolls = payrollRepository.findByEmployeeIdOrderByPayDateDesc(emp.getId());
                } else {
                    payrolls = List.of();
                }
            } else {
                payrolls = List.of();
            }
        }

        model.addAttribute("payrolls", payrolls);
        return "payroll/list";
    }

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("currentPage", "payroll");
        model.addAttribute("payroll", new Payroll());
        model.addAttribute("employees", employeeRepository.findAll());
        return "payroll/generate";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long employeeId,
                       @RequestParam BigDecimal basicSalary,
                       @RequestParam BigDecimal allowances,
                       @RequestParam BigDecimal deductions,
                       @RequestParam String payPeriod,
                       RedirectAttributes redirect) {
        if (basicSalary.compareTo(BigDecimal.ZERO) < 0
                || allowances.compareTo(BigDecimal.ZERO) < 0
                || deductions.compareTo(BigDecimal.ZERO) < 0) {
            redirect.addFlashAttribute("errorMessage", "Salary, allowances, and deductions must be >= 0.");
            return "redirect:/payroll/generate";
        }

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            redirect.addFlashAttribute("errorMessage", "Employee not found.");
            return "redirect:/payroll/generate";
        }

        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setBasicSalary(basicSalary);
        payroll.setAllowances(allowances);
        payroll.setDeductions(deductions);
        payroll.setNetSalary(basicSalary.add(allowances).subtract(deductions));
        payroll.setPayPeriod(payPeriod);
        payroll.setPayDate(LocalDate.now());
        payroll.setStatus("PAID");
        payrollRepository.save(payroll);

        redirect.addFlashAttribute("successMessage", "Payroll generated successfully.");
        return "redirect:/payroll";
    }

    @GetMapping("/payslip/{id}")
    public String payslip(@PathVariable Long id, Model model, Authentication auth) {
        Payroll payroll = payrollRepository.findById(id).orElse(null);
        if (payroll == null) {
            return "redirect:/payroll";
        }

        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user == null) {
                return "redirect:/payroll";
            }
            Employee emp = employeeRepository.findByUserId(user.getId()).orElse(null);
            if (emp == null || !emp.getId().equals(payroll.getEmployee().getId())) {
                return "redirect:/payroll";
            }
        }

        model.addAttribute("currentPage", "payroll");
        model.addAttribute("payslip", payroll);
        return "payroll/payslip";
    }
}
