package com.staffsync.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "pay_date", nullable = false)
    private LocalDate payDate;

    @Column(name = "basic_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal basicSalary;

    @Column(precision = 10, scale = 2)
    private BigDecimal allowances;

    @Column(precision = 10, scale = 2)
    private BigDecimal deductions;

    @Column(name = "net_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal netSalary;

    @Column(name = "pay_period")
    private String payPeriod;

    @Column(nullable = false)
    private String status;
}
