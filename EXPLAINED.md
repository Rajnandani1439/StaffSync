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

## Sprint 4 - Attendance Module

Status: Pending

Features:

* Mark Attendance
* View Attendance
* Attendance History

Deliverable:

Attendance records stored in PostgreSQL.

---

## Sprint 5 - Leave Module

Status: Pending

Features:

* Apply Leave
* Approve Leave
* Reject Leave
* Leave History

Deliverable:

Leave workflow operational.

---

## Sprint 6 - Payroll Module

Status: Pending

Features:

* Salary Records
* Generate Payslip
* Payroll History

Formula:

Net Salary =
Basic Salary +
Allowance -
Deduction

Deliverable:

Payroll module functional.

---

## Sprint 7 - Security

Status: Optional

Implement only if time permits.

Roles:

* ADMIN
* HR
* EMPLOYEE

Deliverable:

Role-based access control.

---

## Sprint 8 - Reports

Status: Optional

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
6. Explain Attendance Workflow.
7. Explain Leave Approval Workflow.
8. Explain Payroll Module.
9. Explain Security Mechanism.
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

## NEXT SPRINT

Sprint 4 – Attendance Module