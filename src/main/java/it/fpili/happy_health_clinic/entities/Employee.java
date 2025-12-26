package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Abstract entity representing an employee.
 * Extends User with employment-specific information.
 * Serves as a base class for specific employee types (Doctor, Staff).
 */
@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Employee extends User {

    /**
     * The employee's unique code identifier.
     * Must be unique across all employees.
     */
    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    /**
     * The date the employee was hired.
     */
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    /**
     * The employee's salary amount.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    /**
     * The current employment status of the employee.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "employee_status", nullable = false, length = 50)
    private EmploymentStatus employeeStatus;

    /**
     * The department or clinic section the employee works in.
     */
    @Column(nullable = false, length = 100)
    private String department;
}
