package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new doctor record.
 * Contains the required information to register a doctor in the system.
 */
@Data
public class CreateDoctorRequestDTO {

    /**
     * The doctor's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * The doctor's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * The doctor's email address.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The doctor's password.
     * Must contain at least 8 characters including uppercase, lowercase, digit, and special character.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    /**
     * The doctor's phone number.
     */
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    /**
     * The doctor's date of birth.
     * Must be in the past.
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * The doctor's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The date the doctor was hired.
     * Cannot be in the future.
     */
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    /**
     * The doctor's salary amount.
     */
    private BigDecimal salary;

    /**
     * The doctor's employment status.
     */
    @NotNull(message = "Employment status is required")
    private EmploymentStatus employeeStatus;

    /**
     * The department or clinic section the doctor works in.
     */
    @NotBlank(message = "Department is required")
    private String department;

    /**
     * The doctor's medical license number.
     */
    @NotBlank(message = "License number is required")
    private String licenseNumber;

    /**
     * The doctor's medical specialization.
     */
    @NotBlank(message = "Specialization is required")
    private String specialization;

    /**
     * A biographical description of the doctor.
     */
    private String biography;
}