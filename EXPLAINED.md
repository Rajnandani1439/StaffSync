# EXPLAINED.md

# StaffSync - Smart Human Resource Management System

## Project Status

Current Phase: College Project Development

Goal:

Build a professional Human Resource Management System suitable for:

* B.Sc IT Final Year Project
* Viva Demonstration
* Report Submission
* PPT Demonstration
* GitHub Portfolio

This is NOT intended to be an enterprise-level HRMS.

The project should remain simple, maintainable, and easy to demonstrate.

---

# Project Scope

Implemented Modules:

* Authentication (Basic)
* Dashboard
* Employee Management
* Attendance Management
* Leave Management
* Payroll Management
* Reports

Future Scope (NOT TO BE IMPLEMENTED):

* AI Analytics
* Mobile App
* Cloud Deployment
* Biometric Attendance
* Email Notifications
* SMS Notifications
* Docker
* Microservices
* JWT Authentication
* AWS Deployment

---

# Tech Stack

## Frontend

* Thymeleaf
* HTML5
* CSS3
* Bootstrap 5
* JavaScript

## Backend

* Spring Boot 3
* Spring MVC

## Database

* PostgreSQL

## ORM

* Spring Data JPA
* Hibernate

## Security

* Spring Security (Basic Session Authentication)

## Build Tool

* Maven

## Version Control

* GitHub

---

# Repository Structure

```text
staffsync/

src/main/java/com/staffsync

├── controller
├── service
├── repository
├── model
├── config
├── security
├── dto
└── exception

src/main/resources

├── templates
├── static
└── application.yml
```

---

# Architecture

```text
Browser
   ↓
Thymeleaf UI
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

Strict layered architecture must be followed.

Controllers must not directly access repositories.

---

# Sprint Progress

## Sprint 0 - Architecture

Status: Completed

Completed:

* Project Planning
* Architecture Design
* Repository Blueprint
* Sprint Planning
* Report Structure

---

## Sprint 1 - Frontend UI

Status: Completed

Completed Pages:

* Login
* Dashboard
* Employees
* Employee Add
* Employee Edit
* Employee View
* Attendance
* Attendance Report
* Leave
* Leave Apply
* Payroll
* Payslip
* Reports

Notes:

UI is screenshot-ready.

Screenshots from these pages are used in:

* Project Report
* PPT
* Viva

Do not redesign UI unless required for bug fixing.

---

# Remaining Work

## Sprint 2 - Database Foundation

Status: Completed

Completed:

* PostgreSQL connected and running
* Database `staffsync` created
* Hibernate auto-schema generation working
* 6 JPA entities created with correct relationships
* 6 repositories created (1:1 with entities)
* Application boots successfully on port 8080
* All 6 tables auto-generated with PKs, FKs, unique constraints, sequences

---

## Sprint 3 - Employee CRUD

Status: Completed

Completed Features:

* Employee List — reads from PostgreSQL via `EmployeeRepository.findAll()`
* Add Employee — form creates new record in DB
* View Employee — profile page shows DB values
* Edit Employee — changes persist to PostgreSQL
* Delete Employee — removes record from DB

Changes Made:

* Added `status` field to Employee entity (was missing from Sprint 2)
* Replaced mock `Map<String, Object>` controller with `EmployeeRepository`-backed CRUD
* Added form binding (`th:object`, `th:field`) to add/edit templates
* Updated templates: `position` → `designation` to match entity field name

Definition of Done:

* CRUD working
* Data stored in PostgreSQL
* Employee pages connected to database
* `mvn clean compile` passes
* Application starts successfully

---

## Sprint 4 - Authentication & Role Management

Status: Completed

Completed Features:

* Spring Security with form-based login
* BCrypt password encoding
* Session-based authentication
* Login page at `/auth/login`
* Logout support (POST `/auth/logout`)
* Role-based access control (ADMIN vs EMPLOYEE)
* Seeded roles: ADMIN, EMPLOYEE
* Seeded admin user: `admin` / `admin123`

Access Rules:

* ADMIN: Dashboard, Employees, Attendance, Leave, Payroll, Reports
* EMPLOYEE: Dashboard only

New Files Created:

* `security/CustomUserDetailsService.java` — loads users from DB
* `security/SecurityConfig.java` — filter chain, login/logout config
* `config/DataSeeder.java` — seeds roles and admin on startup

Files Modified:

* `pom.xml` — added spring-boot-starter-security, thymeleaf-extras-springsecurity6
* `repository/UserRepository.java` — added `findByUsername` with JOIN FETCH
* `repository/RoleRepository.java` — added `findByName`
* `controller/AuthController.java` — handles login error/logout params
* `templates/auth/login.html` — form submits to Spring Security, shows errors
* `templates/fragments/sidebar.html` — role-based menu visibility with `sec:authorize`
* `templates/fragments/navbar.html` — logout uses POST form

Deliverable:

Application secured with role-based access.

---

## Sprint 5 - Attendance Module

Status: Completed

Completed Features:

* Attendance List — reads from PostgreSQL via `AttendanceRepository`
* Mark Attendance — form saves record with duplicate date+employee validation
* Attendance Report — monthly summary with present/absent/late/half-day counts and percentage
* Today's stats cards (Present, Late, Absent, Half Day)
* ADMIN: full attendance access
* EMPLOYEE: view own attendance only

Files Modified:

* `controller/AttendanceController.java` — replaced mock data with repository-backed CRUD
* `repository/AttendanceRepository.java` — added queries: by date, by employee, duplicate check, status counts
* `repository/EmployeeRepository.java` — added `findByUserId` for EMPLOYEE role mapping
* `security/SecurityConfig.java` — attendance routes split: mark/save/report require ADMIN, list requires auth
* `templates/attendance/list.html` — entity-based fields, dynamic stat cards
* `templates/attendance/mark.html` — form binding, employee dropdown from DB, duplicate error display
* `templates/attendance/report.html` — dynamic month display, fixed percentage calculation

Deliverable:

Attendance records stored in PostgreSQL.

---

## Sprint 6 - Leave Module

Status: Completed

Completed Features:

* Apply Leave — form auto-assigns logged-in employee (via User → Employee mapping)
* Approve Leave — ADMIN-only POST endpoint, updates status to APPROVED
* Reject Leave — ADMIN-only POST endpoint, updates status to REJECTED
* Leave History — ADMIN sees all leaves; EMPLOYEE sees own leaves only
* Date validation — end date must be ≥ start date
* Server-side validation with flash error display

Files Modified:

* `controller/LeaveController.java` — replaced mock data with DB-backed CRUD:
  - `list()`: ADMIN sees all via `findAllByOrderByStartDateDesc()`, EMPLOYEE sees own via `findByEmployeeIdOrderByStartDateDesc()`
  - `save()`: validates dates, looks up Employee from logged-in User, sets status=PENDING and appliedOn=now
  - `approve()`: sets status=APPROVED
  - `reject()`: sets status=REJECTED
* `repository/LeaveRequestRepository.java` — added `findAllByOrderByStartDateDesc()` and `findByEmployeeIdOrderByStartDateDesc()`
* `security/SecurityConfig.java` — leave routes: approve/reject require ADMIN, all other leave paths require authentication
* `config/DataSeeder.java` — now creates Employee record linked to admin user (required for leave submission)
* `templates/leave/list.html` — replaced mock field references with entity fields (`leave.employee.firstName`, `leave.leaveType`, `leave.startDate`, `leave.endDate`, `leave.appliedOn`, `leave.status`, `leave.reason`); added approve/reject POST forms with CSRF, visible only to ADMIN via `sec:authorize`
* `templates/leave/apply.html` — form binding with `th:action`, `th:object`, `th:field`; removed employee dropdown (auto-assigned); added error/success flash message alerts
* `templates/fragments/sidebar.html` — removed ADMIN-only restriction on Leave nav link so EMPLOYEE can access

Important Notes:

* Leave auto-assigns the logged-in employee via `User.findByUsername` → `Employee.findByUserId` — no employee selector in the form
* CSRF is enabled globally; all POST forms include `_csrf` (auto-injected with `th:action` + `method="post"`)
* The admin user must have a linked Employee record in the database (handled by DataSeeder)

Deliverable:

Leave workflow operational.

---

## Sprint 7 - Payroll Module

Status: Completed

Completed Features:

* Payroll List — ADMIN sees all records; EMPLOYEE sees own only
* Generate Payroll — ADMIN-only form with employee dropdown, basic salary, allowances, deductions, pay period
* Automatic net salary calculation: Net = Basic + Allowances − Deductions
* Payslip View — dynamic page with employee info, salary breakdown, net salary
* Validation — salary, allowances, deductions must be ≥ 0 (with flash error display)
* Access control — generate/save routes require ADMIN; list/payslip routes are authenticated with employee-level filtering

Files Modified:

* `model/Payroll.java` — added `status` column (set to "PAID" on generation)
* `repository/PayrollRepository.java` — added `findAllByOrderByPayDateDesc()` and `findByEmployeeIdOrderByPayDateDesc()`
* `security/SecurityConfig.java` — split payroll routes: `/generate` and `/save` require ADMIN, others require authentication
* `templates/payroll/list.html` — replaced mock map fields with entity fields (`pay.employee.firstName`, `pay.basicSalary`, `pay.netSalary`, `pay.allowances`, `pay.deductions`, `pay.payPeriod`, `pay.status`); Generate button only visible to ADMIN via `sec:authorize`
* `templates/payroll/generate.html` — form binding with employee dropdown from DB, numeric inputs with min=0, validation error/success alerts, CSRF protection
* `templates/payroll/payslip.html` — replaced mock map references with entity fields; employee name, ID, department, designation from `payslip.employee.*`; salary breakdown from `payslip.basicSalary`, `payslip.allowances`, `payslip.deductions`, `payslip.netSalary`; removed bank/PAN mock data

New Files Created:

* `controller/PayrollController.java` — full CRUD controller:
  - `list()`: ADMIN sees all, EMPLOYEE sees own via User → Employee mapping
  - `generateForm()`: passes employee list and empty Payroll object
  - `save()`: validates ≥ 0, calculates net salary, sets status="PAID" and payDate=now
  - `payslip()`: loads by ID, enforces employee ownership for non-ADMIN users

Deliverable:

Payroll module functional.

---

## Sprint 8 - Reports

Status: Pending

Features:

* Employee Report
* Attendance Report
* Leave Report
* Payroll Report

Simple tables are sufficient.

Charts are optional.

---

# Development Rules

## Rule 1

Never redesign architecture.

Architecture must remain:

Controller
→ Service
→ Repository
→ Database

---

## Rule 2

Never introduce:

* React
* Angular
* Vue
* Microservices
* Kafka
* Redis
* Docker
* Kubernetes

---

## Rule 3

Do not implement future scope features.

Only implement features listed under active sprints.

---

## Rule 4

Always fix compilation errors before adding new features.

---

## Rule 5

Every sprint must end with:

* Working application
* Successful build
* Manual testing

---

# Viva Focus

Examiner will likely ask:

1. What problem does StaffSync solve?
2. Why Spring Boot?
3. Why PostgreSQL?
4. Explain MVC Architecture.
5. Explain Entity Relationships.
6. Explain Security Mechanism (Spring Security, BCrypt, Role-based access).
7. Explain Attendance Workflow.
8. Explain Leave Approval Workflow.
9. Explain Payroll Module.
10. Explain Future Scope.

All development should support answering these questions.

---

# Success Criteria

Project is considered complete when:

* Employee CRUD works
* Attendance works
* Leave works
* Payroll works
* Reports display data
* Application runs without errors
* Screenshots match report
* Viva demo can be completed in under 10 minutes

END OF FILE

## CURRENT STATUS

Sprint 1: Complete
Sprint 2: Complete
Sprint 3: Complete
Sprint 4: Complete
Sprint 5: Complete
Sprint 6: Complete
Sprint 7: Complete

## NEXT SPRINT

Sprint 8 – Reports