package com.staffsync.config;

import com.staffsync.model.Employee;
import com.staffsync.model.Role;
import com.staffsync.model.User;
import com.staffsync.repository.EmployeeRepository;
import com.staffsync.repository.RoleRepository;
import com.staffsync.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN", "Administrator", null));
            roleRepository.save(new Role(null, "EMPLOYEE", "Employee", null));
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@staffsync.com");
            admin.setEnabled(true);
            admin.setRole(adminRole);
            userRepository.save(admin);

            Employee adminEmp = new Employee();
            adminEmp.setFirstName("Admin");
            adminEmp.setLastName("User");
            adminEmp.setEmail("admin@staffsync.com");
            adminEmp.setDepartment("Administration");
            adminEmp.setDesignation("System Administrator");
            adminEmp.setStatus("ACTIVE");
            adminEmp.setUser(admin);
            employeeRepository.save(adminEmp);
        } else {
            // Ensure admin user has a linked Employee record
            User admin = userRepository.findByUsername("admin").get();
            if (employeeRepository.findByUserId(admin.getId()).isEmpty()) {
                Employee adminEmp = new Employee();
                adminEmp.setFirstName("Admin");
                adminEmp.setLastName("User");
                adminEmp.setEmail("admin@staffsync.com");
                adminEmp.setDepartment("Administration");
                adminEmp.setDesignation("System Administrator");
                adminEmp.setStatus("ACTIVE");
                adminEmp.setUser(admin);
                employeeRepository.save(adminEmp);
            }
        }
    }
}