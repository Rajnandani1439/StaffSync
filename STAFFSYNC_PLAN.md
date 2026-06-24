# STAFFSYNC — Smart Human Resource Management System

## Architecture & Implementation Plan

---

# PART 1: HIGH-LEVEL ARCHITECTURE

## 1. High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT (Browser)                         │
│  HTML5 + CSS3 + Bootstrap 5 + Thymeleaf Templates          │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP (GET/POST/PUT/DELETE)
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  SPRING BOOT 3 APPLICATION                  │
│                                                             │
│  ┌───────────┐  ┌───────────┐  ┌───────────────────────┐  │
│  │ Thymeleaf │  │ Spring    │  │ Spring Security       │  │
│  │ Templates │  │ Web MVC   │  │ (Session-based Auth)  │  │
│  └───────────┘  └───────────┘  └───────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              LAYERED ARCHITECTURE                   │   │
│  │  Controller → Service → Repository → Entity         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC / JPA
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      MySQL DATABASE                         │
│  staffsync_db                                              │
│  Tables: role, user, employee, attendance,                 │
│          leave_request, payroll                             │
└─────────────────────────────────────────────────────────────┘
```

## 2. Layered Architecture

```
┌─────────────────────────────────────────────────┐
│  PRESENTATION LAYER (Thymeleaf + Controllers)   │
│  - Handles HTTP requests/responses               │
│  - Renders HTML views with Thymeleaf             │
│  - Form validation (client + server side)        │
├─────────────────────────────────────────────────┤
│  SERVICE LAYER (Business Logic)                 │
│  - Contains all business rules                   │
│  - Transaction management (@Transactional)       │
│  - DTO conversion                                │
│  - Exception handling                            │
├─────────────────────────────────────────────────┤
│  REPOSITORY LAYER (Data Access)                 │
│  - Spring Data JPA repositories                  │
│  - Custom queries (@Query)                       │
│  - Pagination and sorting                        │
├─────────────────────────────────────────────────┤
│  ENTITY LAYER (Domain Model)                    │
│  - JPA entities mapped to database tables        │
│  - Relationships (OneToMany, ManyToOne)          │
│  - Column mappings and constraints               │
├─────────────────────────────────────────────────┤
│  DATABASE (MySQL)                                │
└─────────────────────────────────────────────────┘
```

## 3. Package Structure

```
com.staffsync
├── config          → App config, beans, WebMvcConfig
├── security        → SecurityConfig, AuthProvider, LoginSuccessHandler
├── controller      → Web controllers (Thymeleaf)
│   ├── auth        → LoginController, RegistrationController
│   ├── employee    → EmployeeController
│   ├── attendance  → AttendanceController
│   ├── leave       → LeaveController
│   ├── payroll     → PayrollController
│   ├── report      → ReportController
│   └── dashboard   → DashboardController
├── service         → Business logic interfaces + implementations
│   ├── impl        → Service implementations
│   └── mapper      → Entity ↔ DTO mapping helpers
├── repository      → JPA repositories
├── model           → JPA entities
│   └── enums       → Enum types (RoleType, LeaveStatus, etc.)
├── dto             → Data Transfer Objects
│   ├── request     → Incoming form data wrappers
│   └── response    → Outgoing view data wrappers
└── exception       → Custom exceptions + GlobalExceptionHandler
```

## 4. Folder Structure

```
staffsync/
├── pom.xml
├── README.md
├── .gitignore
├── staffsync.sql                         # Database schema script
│
├── src/
│   ├── main/
│   │   ├── java/com/staffsync/
│   │   │   ├── StaffSyncApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── WebMvcConfig.java
│   │   │   │   └── AppConfig.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── EmployeeController.java
│   │   │   │   ├── AttendanceController.java
│   │   │   │   ├── LeaveController.java
│   │   │   │   ├── PayrollController.java
│   │   │   │   └── ReportController.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── EmployeeService.java
│   │   │   │   ├── AttendanceService.java
│   │   │   │   ├── LeaveService.java
│   │   │   │   ├── PayrollService.java
│   │   │   │   └── ReportService.java
│   │   │   │
│   │   │   ├── service/impl/
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── EmployeeServiceImpl.java
│   │   │   │   ├── AttendanceServiceImpl.java
│   │   │   │   ├── LeaveServiceImpl.java
│   │   │   │   ├── PayrollServiceImpl.java
│   │   │   │   └── ReportServiceImpl.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── RoleRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   ├── AttendanceRepository.java
│   │   │   │   ├── LeaveRequestRepository.java
│   │   │   │   └── PayrollRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── Role.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Employee.java
│   │   │   │   ├── Attendance.java
│   │   │   │   ├── LeaveRequest.java
│   │   │   │   └── Payroll.java
│   │   │   │
│   │   │   ├── model/enums/
│   │   │   │   ├── RoleType.java
│   │   │   │   ├── LeaveStatus.java
│   │   │   │   ├── AttendanceStatus.java
│   │   │   │   └── PayrollStatus.java
│   │   │   │
│   │   │   ├── dto/request/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── EmployeeRequest.java
│   │   │   │   ├── AttendanceRequest.java
│   │   │   │   ├── LeaveRequestDto.java
│   │   │   │   └── PayrollRequest.java
│   │   │   │
│   │   │   ├── dto/response/
│   │   │   │   ├── EmployeeResponse.java
│   │   │   │   ├── AttendanceResponse.java
│   │   │   │   ├── LeaveResponse.java
│   │   │   │   ├── PayrollResponse.java
│   │   │   │   └── DashboardResponse.java
│   │   │   │
│   │   │   └── exception/
│   │   │       ├── ResourceNotFoundException.java
│   │   │       ├── BadRequestException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   │
│   │   ├── resources/
│   │   │   ├── application.yml
│   │   │   ├── messages.properties
│   │   │   │
│   │   │   ├── static/
│   │   │   │   ├── css/
│   │   │   │   │   ├── staffsync.css
│   │   │   │   │   └── login.css
│   │   │   │   ├── js/
│   │   │   │   │   ├── main.js
│   │   │   │   │   └── validation.js
│   │   │   │   └── images/
│   │   │   │       └── logo.png
│   │   │   │
│   │   │   └── templates/
│   │   │       ├── fragments/
│   │   │       │   ├── header.html
│   │   │       │   ├── sidebar.html
│   │   │       │   ├── footer.html
│   │   │       │   └── layout.html
│   │   │       │
│   │   │       ├── auth/
│   │   │       │   ├── login.html
│   │   │       │   └── register.html
│   │   │       │
│   │   │       ├── dashboard/
│   │   │       │   └── dashboard.html
│   │   │       │
│   │   │       ├── employee/
│   │   │       │   ├── list.html
│   │   │       │   ├── add.html
│   │   │       │   ├── edit.html
│   │   │       │   └── view.html
│   │   │       │
│   │   │       ├── attendance/
│   │   │       │   ├── list.html
│   │   │       │   ├── add.html
│   │   │       │   └── report.html
│   │   │       │
│   │   │       ├── leave/
│   │   │       │   ├── list.html
│   │   │       │   ├── apply.html
│   │   │       │   └── approve.html
│   │   │       │
│   │   │       ├── payroll/
│   │   │       │   ├── list.html
│   │   │       │   ├── generate.html
│   │   │       │   └── payslip.html
│   │   │       │
│   │   │       ├── report/
│   │   │       │   └── reports.html
│   │   │       │
│   │   │       └── error/
│   │   │           └── 404.html
│   │   │
│   │   └── webapp/
│   │       └── WEB-INF/       (not needed — Thymeleaf handles views)
│   │
│   └── test/
│       └── java/com/staffsync/
│           ├── StaffSyncApplicationTests.java
│           ├── controller/
│           ├── service/
│           └── repository/
│
└── target/                     (generated)
```

## 5. Module Structure

| # | Module | Controller | Service | Repository | Entity | Pages |
|---|--------|-----------|---------|------------|--------|-------|
| 1 | Auth | AuthController | UserService | UserRepository, RoleRepository | User, Role | login, register |
| 2 | Dashboard | DashboardController | — | — | — | dashboard |
| 3 | Employee | EmployeeController | EmployeeService | EmployeeRepository | Employee | list, add, edit, view |
| 4 | Attendance | AttendanceController | AttendanceService | AttendanceRepository | Attendance | list, add, report |
| 5 | Leave | LeaveController | LeaveService | LeaveRequestRepository | LeaveRequest | list, apply, approve |
| 6 | Payroll | PayrollController | PayrollService | PayrollRepository | Payroll | list, generate, payslip |
| 7 | Report | ReportController | ReportService | All repositories | — | reports |

## 6. Database Entity Plan

| Entity | Table | Key Feature |
|--------|-------|-------------|
| Role | role | Enum-based roles (ADMIN, HR, MANAGER, EMPLOYEE) |
| User | user | Login credentials, maps to Employee |
| Employee | employee | Personal info, department, position, salary |
| Attendance | attendance | Daily check-in/out with status |
| LeaveRequest | leave_request | Leave applications with approval workflow |
| Payroll | payroll | Monthly salary records per employee |

## 7. Controller Plan

| Controller | Base URL | Endpoints |
|-----------|----------|-----------|
| AuthController | /auth | GET /login, POST /login, GET /register, POST /register, GET /logout |
| DashboardController | /dashboard | GET /dashboard |
| EmployeeController | /employees | GET /, GET /add, POST /save, GET /edit/{id}, POST /update, GET /delete/{id}, GET /{id} |
| AttendanceController | /attendance | GET /, GET /mark, POST /save, GET /report |
| LeaveController | /leave | GET /, GET /apply, POST /submit, GET /approve/{id}, POST /process |
| PayrollController | /payroll | GET /, GET /generate, POST /save, GET /payslip/{id} |
| ReportController | /reports | GET /, GET /employees, GET /attendance, GET /payroll |

## 8. Service Plan

| Service | Key Methods |
|---------|-------------|
| UserService | authenticate(), registerUser(), findByUsername(), findAll() |
| EmployeeService | getAllEmployees(), getEmployeeById(), saveEmployee(), updateEmployee(), deleteEmployee() |
| AttendanceService | markAttendance(), getAttendanceByEmployee(), getAttendanceByDate(), getMonthlyReport() |
| LeaveService | applyLeave(), getLeavesByEmployee(), getAllPendingLeaves(), approveLeave(), rejectLeave() |
| PayrollService | generatePayroll(), getPayrollByEmployee(), getAllPayrolls(), getPayslip() |
| ReportService | generateEmployeeReport(), generateAttendanceReport(), generatePayrollReport() |

## 9. Repository Plan

| Repository | Entity | Custom Queries Needed |
|------------|--------|----------------------|
| RoleRepository | Role | findByRoleType() |
| UserRepository | User | findByUsername(), findByEmail() |
| EmployeeRepository | Employee | findByDepartment(), findByUser() |
| AttendanceRepository | Attendance | findByEmployeeAndDate(), findByEmployeeAndMonth() |
| LeaveRequestRepository | LeaveRequest | findByEmployee(), findByStatus(), findByDateRange() |
| PayrollRepository | Payroll | findByEmployeeAndMonth(), findByEmployeeOrderByDateDesc() |

## 10. Security Plan

```
Spring Security (Session-based, NO JWT)

┌──────────────────────────────────────────────────┐
│  SecurityConfig.java                             │
│                                                   │
│  - HttpSecurity configuration                    │
│  - Role-based access:                             │
│    ADMIN    → Full access to all modules          │
│    HR       → Employee, Attendance, Leave        │
│    MANAGER  → Attendance, Leave (team)           │
│    EMPLOYEE → Own attendance, own leave          │
│                                                   │
│  - Login page at /auth/login                     │
│  - Default success URL: /dashboard               │
│  - Logout: /auth/logout                          │
│  - Remember-me support                           │
│  - BCrypt password encoding                      │
│  - Session management                            │
└──────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────┐
│  CustomUserDetailsService.java                   │
│                                                   │
│  - Loads User from DB by username                │
│  - Maps to Spring Security UserDetails           │
│  - Adds GrantedAuthority from Role               │
└──────────────────────────────────────────────────┘
```

---

# PART 2: DEVELOPMENT ROADMAP

## Phases

```
PHASE 1: Foundation (Sprint 0)
  ├── Project setup, Maven, Spring Boot initialization
  ├── Database schema design
  ├── Security configuration
  ├── Base layouts (header, sidebar, footer)
  └── Theme and navigation

PHASE 2: Authentication (Sprint 1)
  ├── Login page with validation
  ├── Registration page
  ├── Role-based redirect after login
  └── Logout

PHASE 3: Core Features (Sprints 2–4)
  ├── Dashboard with widgets and summaries
  ├── Employee CRUD (full module)
  ├── Attendance marking and reports
  └── Leave management with approval workflow

PHASE 4: Advanced Features (Sprints 5–7)
  ├── Payroll generation and payslip
  ├── Role-based security hardening
  ├── Reporting (combined data views)
  └── Error handling and validation

PHASE 5: Polish & Testing (Sprints 8–9)
  ├── Unit tests and integration tests
  ├── UI cleanup and consistency
  ├── README, screenshots, documentation
  └── GitHub finalization
```

---

# PART 3: SPRINT PLANNING

## Sprint 0 — Project Foundation

| Aspect | Details |
|--------|---------|
| **Goal** | Initialize project, configure dependencies, set up database, establish base layout |
| **Deliverables** | Runnable Spring Boot app, MySQL database with tables, login page visible |
| **Pages** | login.html (placeholder), layout.html fragments |
| **Backend** | Create Spring Boot project with Maven, configure application.yml, set up SecurityConfig, create all entities, create all repositories, create CustomUserDetailsService |
| **Frontend** | Create layout templates (header, sidebar, footer), add Bootstrap 5 CDN, create base CSS |
| **Database** | Run staffsync.sql to create all 6 tables with sample data |

## Sprint 1 — Authentication UI

| Aspect | Details |
|--------|---------|
| **Goal** | Working login and registration with role assignment |
| **Deliverables** | Functional login page, registration page, logout, role-based redirect |
| **Pages** | login.html, register.html |
| **Backend** | AuthController (GET/POST login, GET/POST register), UserService, RoleRepository init |
| **Frontend** | login.html with form, register.html with form, validation.js, login.css |
| **Database** | Seed roles (ADMIN, HR, MANAGER, EMPLOYEE), seed one admin user |

## Sprint 2 — Dashboard

| Aspect | Details |
|--------|---------|
| **Goal** | Dashboard with summary cards and navigation |
| **Deliverables** | Dashboard page showing employee count, attendance today, pending leaves, recent payroll |
| **Pages** | dashboard.html |
| **Backend** | DashboardController, aggregate queries in service layer |
| **Frontend** | dashboard.html with Bootstrap cards, charts (simple CSS), sidebar navigation active state |
| **Database** | No schema changes |

## Sprint 3 — Employee Management

| Aspect | Details |
|--------|---------|
| **Goal** | Complete CRUD for employees |
| **Deliverables** | Employee list, add form, edit form, view profile, delete |
| **Pages** | employee/list.html, employee/add.html, employee/edit.html, employee/view.html |
| **Backend** | EmployeeController (CRUD), EmployeeService, EmployeeRequest/Response DTOs, validation |
| **Frontend** | Data table with search, form with validation, profile card view, delete confirmation modal |
| **Database** | Seed 5–10 sample employees |

## Sprint 4 — Attendance

| Aspect | Details |
|--------|---------|
| **Goal** | Mark attendance daily, view attendance records, monthly report |
| **Deliverables** | Mark attendance page, attendance list, monthly report with summary |
| **Pages** | attendance/list.html, attendance/add.html, attendance/report.html |
| **Backend** | AttendanceController, AttendanceService, date-based queries |
| **Frontend** | Calendar-style or date picker mark form, table view with status badges, report with totals |
| **Database** | Seed sample attendance records for current month |

## Sprint 5 — Leave Management

| Aspect | Details |
|--------|---------|
| **Goal** | Apply for leave, view leaves, approve/reject by HR/Manager |
| **Deliverables** | Leave application form, leave history, approval dashboard |
| **Pages** | leave/list.html, leave/apply.html, leave/approve.html |
| **Backend** | LeaveController, LeaveService, status workflow (PENDING → APPROVED/REJECTED) |
| **Frontend** | Leave form with date range picker, status badges (Pending/Approved/Rejected), approve/reject buttons |
| **Database** | Seed sample leave requests |

## Sprint 6 — Payroll

| Aspect | Details |
|--------|---------|
| **Goal** | Generate monthly payroll, view payslips |
| **Deliverables** | Payroll list, generate payroll form, individual payslip view |
| **Pages** | payroll/list.html, payroll/generate.html, payroll/payslip.html |
| **Backend** | PayrollController, PayrollService (auto-calculate based on attendance + base salary) |
| **Frontend** | Payroll table with status, generate button (protected), printable payslip template |
| **Database** | Seed sample payroll records |

## Sprint 7 — Security Hardening

| Aspect | Details |
|--------|---------|
| **Goal** | Role-based access control, CSRF, session management |
| **Deliverables** | Secure endpoints, access-denied handling, CSRF protection on all forms |
| **Pages** | error/403.html (access denied) |
| **Backend** | SecurityConfig with role-based URL patterns, method-level @PreAuthorize, CSRF token handling |
| **Frontend** | Hide unauthorized menu items, show access denied page |
| **Database** | No schema changes |

## Sprint 8 — Reports

| Aspect | Details |
|--------|---------|
| **Goal** | Generate combined reports (employee listing, attendance summary, payroll summary) |
| **Deliverables** | Reports page with filterable views, export to printable format |
| **Pages** | report/reports.html |
| **Backend** | ReportController, ReportService (aggregate data from multiple entities) |
| **Frontend** | Tabbed report views, date range filters, print-friendly CSS |
| **Database** | No schema changes |

## Sprint 9 — Testing & Polish

| Aspect | Details |
|--------|---------|
| **Goal** | Ensure stability, test all flows, polish UI |
| **Deliverables** | Unit tests for services, integration tests for controllers, UI consistency review |
| **Pages** | All pages reviewed |
| **Backend** | Write JUnit tests for all service methods, write WebMvc tests for controllers |
| **Frontend** | Consistent spacing, colors, responsive design, loading states, flash message styling |
| **Database** | No schema changes |

---

# PART 4: REPOSITORY BLUEPRINT

## Complete File List (pre-coding scaffold)

```
staffsync/
├── pom.xml
├── README.md
├── .gitignore
├── staffsync.sql
│
├── src/main/java/com/staffsync/
│   ├── StaffSyncApplication.java
│   ├── config/
│   │   ├── WebMvcConfig.java
│   │   └── AppConfig.java
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   └── CustomUserDetailsService.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── DashboardController.java
│   │   ├── EmployeeController.java
│   │   ├── AttendanceController.java
│   │   ├── LeaveController.java
│   │   ├── PayrollController.java
│   │   └── ReportController.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── EmployeeService.java
│   │   ├── AttendanceService.java
│   │   ├── LeaveService.java
│   │   ├── PayrollService.java
│   │   └── ReportService.java
│   ├── service/impl/
│   │   ├── UserServiceImpl.java
│   │   ├── EmployeeServiceImpl.java
│   │   ├── AttendanceServiceImpl.java
│   │   ├── LeaveServiceImpl.java
│   │   ├── PayrollServiceImpl.java
│   │   └── ReportServiceImpl.java
│   ├── repository/
│   │   ├── RoleRepository.java
│   │   ├── UserRepository.java
│   │   ├── EmployeeRepository.java
│   │   ├── AttendanceRepository.java
│   │   ├── LeaveRequestRepository.java
│   │   └── PayrollRepository.java
│   ├── model/
│   │   ├── Role.java
│   │   ├── User.java
│   │   ├── Employee.java
│   │   ├── Attendance.java
│   │   ├── LeaveRequest.java
│   │   └── Payroll.java
│   ├── model/enums/
│   │   ├── RoleType.java
│   │   ├── LeaveStatus.java
│   │   ├── AttendanceStatus.java
│   │   └── PayrollStatus.java
│   ├── dto/request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── EmployeeRequest.java
│   │   ├── AttendanceRequest.java
│   │   ├── LeaveRequestDto.java
│   │   └── PayrollRequest.java
│   ├── dto/response/
│   │   ├── EmployeeResponse.java
│   │   ├── AttendanceResponse.java
│   │   ├── LeaveResponse.java
│   │   ├── PayrollResponse.java
│   │   └── DashboardResponse.java
│   └── exception/
│       ├── ResourceNotFoundException.java
│       ├── BadRequestException.java
│       └── GlobalExceptionHandler.java
│
├── src/main/resources/
│   ├── application.yml
│   ├── messages.properties
│   ├── static/
│   │   ├── css/
│   │   │   ├── staffsync.css
│   │   │   └── login.css
│   │   ├── js/
│   │   │   ├── main.js
│   │   │   └── validation.js
│   │   └── images/
│   │       └── .gitkeep
│   └── templates/
│       ├── fragments/
│       │   ├── header.html
│       │   ├── sidebar.html
│       │   ├── footer.html
│       │   └── layout.html
│       ├── auth/
│       │   ├── login.html
│       │   └── register.html
│       ├── dashboard/
│       │   └── dashboard.html
│       ├── employee/
│       │   ├── list.html
│       │   ├── add.html
│       │   ├── edit.html
│       │   └── view.html
│       ├── attendance/
│       │   ├── list.html
│       │   ├── add.html
│       │   └── report.html
│       ├── leave/
│       │   ├── list.html
│       │   ├── apply.html
│       │   └── approve.html
│       ├── payroll/
│       │   ├── list.html
│       │   ├── generate.html
│       │   └── payslip.html
│       ├── report/
│       │   └── reports.html
│       └── error/
│           ├── 404.html
│           └── 403.html
│
└── src/test/java/com/staffsync/
    ├── StaffSyncApplicationTests.java
    ├── controller/
    │   └── .gitkeep
    ├── service/
    │   └── .gitkeep
    └── repository/
        └── .gitkeep
```

**Total: 72 files** (before coding begins)

---

# PART 5: DATABASE BLUEPRINT

## Entity Relationship Diagram (Text)

```
role ──1:N──> user ──1:1──> employee ──1:N──> attendance
                                  │
                                  ├──1:N──> leave_request
                                  │
                                  └──1:N──> payroll
```

## Entity Definitions

### Role

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | |
| role_type | ENUM('ADMIN','HR','MANAGER','EMPLOYEE') | UNIQUE, NOT NULL | |
| description | VARCHAR(100) | | Role description |

**Relationships:** One-to-Many with User

---

### User

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| password | VARCHAR(255) | NOT NULL | BCrypt encoded |
| email | VARCHAR(100) | UNIQUE, NOT NULL | |
| enabled | BOOLEAN | NOT NULL, DEFAULT TRUE | Account active |
| role_id | INT | FK → role(id), NOT NULL | User role |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |
| updated_at | TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | |

**Relationships:** Many-to-One with Role, One-to-One with Employee

---

### Employee

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | Employee ID |
| user_id | INT | FK → user(id), UNIQUE | Link to login |
| first_name | VARCHAR(50) | NOT NULL | |
| last_name | VARCHAR(50) | NOT NULL | |
| email | VARCHAR(100) | NOT NULL | |
| phone | VARCHAR(20) | | |
| department | VARCHAR(50) | NOT NULL | |
| position | VARCHAR(50) | NOT NULL | Job title |
| hire_date | DATE | NOT NULL | |
| salary | DECIMAL(10,2) | NOT NULL | Base monthly salary |
| address | TEXT | | |
| status | ENUM('ACTIVE','INACTIVE','TERMINATED') | NOT NULL | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |

**Relationships:** One-to-One with User, One-to-Many with Attendance, LeaveRequest, Payroll

---

### Attendance

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | |
| employee_id | INT | FK → employee(id), NOT NULL | |
| attendance_date | DATE | NOT NULL | |
| check_in | TIME | | |
| check_out | TIME | | |
| status | ENUM('PRESENT','ABSENT','HALF_DAY','LATE') | NOT NULL | |
| remarks | VARCHAR(255) | | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |

**Unique Constraint:** (employee_id, attendance_date) — one record per employee per day

**Relationships:** Many-to-One with Employee

---

### LeaveRequest

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | |
| employee_id | INT | FK → employee(id), NOT NULL | |
| start_date | DATE | NOT NULL | |
| end_date | DATE | NOT NULL | |
| leave_type | ENUM('SICK','CASUAL','VACATION','OTHER') | NOT NULL | |
| reason | TEXT | NOT NULL | |
| status | ENUM('PENDING','APPROVED','REJECTED') | NOT NULL, DEFAULT 'PENDING' | |
| approved_by | INT | FK → employee(id), NULL | Approver |
| applied_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |
| updated_at | TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | |

**Relationships:** Many-to-One with Employee (applicant), Many-to-One with Employee (approver)

---

### Payroll

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | INT | PK, AUTO_INCREMENT | |
| employee_id | INT | FK → employee(id), NOT NULL | |
| month | DATE | NOT NULL | First day of month |
| basic_salary | DECIMAL(10,2) | NOT NULL | |
| allowances | DECIMAL(10,2) | DEFAULT 0.00 | |
| deductions | DECIMAL(10,2) | DEFAULT 0.00 | |
| net_salary | DECIMAL(10,2) | NOT NULL | basic + allowances - deductions |
| status | ENUM('PENDING','PAID','CANCELLED') | NOT NULL | |
| paid_date | DATE | | |
| remarks | VARCHAR(255) | | |

**Unique Constraint:** (employee_id, month) — one payroll per employee per month

**Relationships:** Many-to-One with Employee

## SQL Schema (staffsync.sql)

```sql
CREATE DATABASE IF NOT EXISTS staffsync_db;
USE staffsync_db;

-- Role table
CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_type ENUM('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE') NOT NULL UNIQUE,
    description VARCHAR(100)
);

-- User table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Employee table
CREATE TABLE employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL,
    hire_date DATE NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    address TEXT,
    status ENUM('ACTIVE', 'INACTIVE', 'TERMINATED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Attendance table
CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    status ENUM('PRESENT', 'ABSENT', 'HALF_DAY', 'LATE') NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (employee_id, attendance_date)
);

-- Leave Request table
CREATE TABLE leave_request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    leave_type ENUM('SICK', 'CASUAL', 'VACATION', 'OTHER') NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    approved_by INT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES employee(id)
);

-- Payroll table
CREATE TABLE payroll (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    month DATE NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    allowances DECIMAL(10, 2) DEFAULT 0.00,
    deductions DECIMAL(10, 2) DEFAULT 0.00,
    net_salary DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'PAID', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    paid_date DATE,
    remarks VARCHAR(255),
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    UNIQUE KEY unique_payroll (employee_id, month)
);
```

---

# PART 6: IMPLEMENTATION ORDER

## Build Order (Optimized for screenshots, viva, and speed)

```
PRIORITY 1 — BUILD FIRST (Sprint 0-1)
═══════════════════════════════════════
  Day 1  │ 1. pom.xml + application.yml (Spring Boot project)
         │ 2. StaffSyncApplication.java (main class)
         │ 3. All 6 entity classes + enums (model/)
         │ 4. staffsync.sql → run on MySQL
         │
  Day 2  │ 5. SecurityConfig + CustomUserDetailsService
         │ 6. RoleRepository + UserRepository
         │ 7. AuthController + login.html + register.html
         │ 8. layout.html, header.html, sidebar.html, footer.html
         │
  RESULT │ ✓ Application RUNS
         │ ✓ Login page visible
         │ ✓ Can register and login
         │ ✓ SCREENSHOT: Login page

PRIORITY 2 — BUILD SECOND (Sprint 2-3)
═══════════════════════════════════════
  Day 3  │ 9. DashboardController + dashboard.html
         │ 10. Employee entity improvements
         │ 11. EmployeeRepository + EmployeeService + impl
         │
  Day 4  │ 12. EmployeeController + employee CRUD pages
         │ 13. Seed 10 employees in database
         │
  RESULT │ ✓ Dashboard shows stats
         │ ✓ Employee CRUD works
         │ ✓ SCREENSHOT: Dashboard with cards
         │ ✓ SCREENSHOT: Employee list table
         │ ✓ SCREENSHOT: Add/Edit employee form

PRIORITY 3 — BUILD THIRD (Sprint 4-5)
═══════════════════════════════════════
  Day 5  │ 14. AttendanceRepository + AttendanceService
         │ 15. AttendanceController + attendance pages
         │ 16. Mark attendance form with date picker
         │
  Day 6  │ 17. LeaveRequestRepository + LeaveService
         │ 18. LeaveController + leave pages
         │ 19. Approval workflow (HR/Manager)
         │
  RESULT │ ✓ Attendance marking works
         │ ✓ Leave application + approval works
         │ ✓ SCREENSHOT: Attendance report
         │ ✓ SCREENSHOT: Leave request form
         │ ✓ SCREENSHOT: Leave approval page

PRIORITY 4 — BUILD LATER (Sprint 6-7)
═══════════════════════════════════════
  Day 7  │ 20. PayrollRepository + PayrollService
         │ 21. PayrollController + payroll pages
         │ 22. Payslip view (printable)
         │
  Day 8  │ 23. Security hardening (role checks, access denied)
         │
  RESULT │ ✓ Payroll generation works
         │ ✓ Payslip printable
         │ ✓ SCREENSHOT: Payroll list
         │ ✓ SCREENSHOT: Payslip

PRIORITY 5 — FINAL (Sprint 8-9)
═══════════════════════════════════════
  Day 9  │ 24. ReportController + reports page
         │ 25. Aggregate queries (employee, attendance, payroll)
         │
  Day 10 │ 26. Unit tests (service layer)
         │ 27. UI polish (consistent styling)
         │ 28. README.md with screenshots
         │ 29. Git init + push to GitHub
         │
  RESULT │ ✓ Reports working
         │ ✓ Tests passing
         │ ✓ GitHub ready
         │ ✓ READY FOR VIVA
```

## Viva Preparation Tips

| Question Topic | What to Say |
|---------------|-------------|
| Architecture | "Monolithic Spring Boot with 3-tier layered architecture — Controller → Service → Repository" |
| Security | "Spring Security with session-based auth, BCrypt passwords, role-based access for Admin/HR/Manager/Employee" |
| Database | "MySQL with 6 tables — normalized with proper foreign keys and unique constraints" |
| Why this stack? | "Spring Boot is industry standard for Java web apps. Thymeleaf integrates natively. Bootstrap gives responsive UI without heavy JS frameworks." |
| Challenges | "Managing attendance uniqueness per day per employee. Implementing leave approval workflow with role-based access." |
| Key Feature | "End-to-end payroll calculation based on attendance and base salary, with printable payslip." |

---

## Quick Start Commands

```bash
# 1. Create database
mysql -u root -p < staffsync.sql

# 2. Run the application
./mvnw spring-boot:run

# 3. Access
#    http://localhost:8080/auth/login

# 4. Default credentials (after seeding)
#    Admin: admin / admin123
```

---

*End of Planning Document*
