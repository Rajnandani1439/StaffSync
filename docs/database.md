# Database Documentation

## Overview

StaffSync uses a PostgreSQL database with 6 tables managed by Hibernate ORM. The schema is auto-generated on application startup (`ddl-auto: update`).

## Entity Relationship Diagram

```
roles
  |
  v
users
  |
  v
employees
  ├── attendance
  ├── leave_requests
  └── payroll
```

## Entities

### 1. Role

**Table:** `roles`

Stores user roles for authorization.

| Column      | Type         | Constraints        |
|-------------|--------------|--------------------|
| id          | BIGSERIAL    | PRIMARY KEY        |
| name        | VARCHAR(255) | NOT NULL, UNIQUE   |
| description | VARCHAR(255) |                    |

**Seeded Data:** ADMIN, EMPLOYEE

---

### 2. User

**Table:** `users`

Stores login credentials linked to a role.

| Column    | Type         | Constraints        |
|-----------|--------------|--------------------|
| id        | BIGSERIAL    | PRIMARY KEY        |
| username  | VARCHAR(255) | NOT NULL, UNIQUE   |
| password  | VARCHAR(255) | NOT NULL (BCrypt)  |
| email     | VARCHAR(255) | NOT NULL, UNIQUE   |
| enabled   | BOOLEAN      | NOT NULL           |
| role_id   | BIGINT       | FOREIGN KEY → roles(id) |

**Relationships:** Many-to-One with Role

---

### 3. Employee

**Table:** `employees`

Stores employee personal and professional details.

| Column      | Type          | Constraints        |
|-------------|---------------|--------------------|
| id          | BIGSERIAL     | PRIMARY KEY        |
| first_name  | VARCHAR(255)  | NOT NULL           |
| last_name   | VARCHAR(255)  | NOT NULL           |
| email       | VARCHAR(255)  | NOT NULL, UNIQUE   |
| phone       | VARCHAR(255)  |                    |
| department  | VARCHAR(255)  |                    |
| designation | VARCHAR(255)  |                    |
| hire_date   | DATE          |                    |
| salary      | DECIMAL(10,2) |                    |
| status      | VARCHAR(255)  | NOT NULL           |
| user_id     | BIGINT        | FOREIGN KEY → users(id), UNIQUE |

**Relationships:** One-to-One with User, One-to-Many with Attendance/LeaveRequest/Payroll

---

### 4. Attendance

**Table:** `attendance`

Tracks daily employee attendance.

| Column      | Type         | Constraints        |
|-------------|--------------|--------------------|
| id          | BIGSERIAL    | PRIMARY KEY        |
| employee_id | BIGINT       | FOREIGN KEY → employees(id), NOT NULL |
| date        | DATE         | NOT NULL           |
| check_in    | TIME         |                    |
| check_out   | TIME         |                    |
| status      | VARCHAR(255) | NOT NULL (Present/Late/Absent/Half Day) |

**Relationships:** Many-to-One with Employee

**Unique Constraint:** One attendance record per employee per day.

---

### 5. LeaveRequest

**Table:** `leave_requests`

Stores employee leave applications.

| Column      | Type          | Constraints        |
|-------------|---------------|--------------------|
| id          | BIGSERIAL     | PRIMARY KEY        |
| employee_id | BIGINT        | FOREIGN KEY → employees(id), NOT NULL |
| start_date  | DATE          | NOT NULL           |
| end_date    | DATE          | NOT NULL           |
| leave_type  | VARCHAR(255)  | NOT NULL           |
| reason      | TEXT          |                    |
| status      | VARCHAR(255)  | NOT NULL (PENDING/APPROVED/REJECTED) |
| applied_on  | TIMESTAMP     | NOT NULL           |

**Relationships:** Many-to-One with Employee

---

### 6. Payroll

**Table:** `payroll`

Stores generated payroll records.

| Column       | Type          | Constraints        |
|--------------|---------------|--------------------|
| id           | BIGSERIAL     | PRIMARY KEY        |
| employee_id  | BIGINT        | FOREIGN KEY → employees(id), NOT NULL |
| pay_date     | DATE          | NOT NULL           |
| basic_salary | DECIMAL(10,2) | NOT NULL           |
| allowances   | DECIMAL(10,2) |                    |
| deductions   | DECIMAL(10,2) |                    |
| net_salary   | DECIMAL(10,2) | NOT NULL           |
| pay_period   | VARCHAR(255)  |                    |
| status       | VARCHAR(255)  | NOT NULL (PAID)    |

**Relationships:** Many-to-One with Employee

**Calculation:** Net Salary = Basic Salary + Allowances − Deductions

---

## Demo Data (Seeded on First Run)

| User     | Role     | Employee       | Attendance            | Leave                         | Payroll                     |
|----------|----------|----------------|-----------------------|-------------------------------|-----------------------------|
| admin    | ADMIN    | Admin User     | —                     | —                             | —                           |
| alice    | EMPLOYEE | Alice Smith    | Present, Present      | Vacation (PENDING), Casual (REJECTED) | $78,000                |
| john     | EMPLOYEE | John Doe       | Late, Absent          | Sick Leave (APPROVED)         | $67,500                     |

## Notes

- All foreign keys are indexed automatically by Hibernate
- Schema is created/updated automatically — no manual SQL migration needed
- The `ddl-auto: update` setting in `application.yml` handles schema evolution
