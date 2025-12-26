package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import it.fpili.happy_health_clinic.enums.StaffRole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for creating a new staff member record.
 * Contains personal and employment information for clinic staff.
 */
@Data
public class CreateStaffRequestDTO {

    /**
     * The staff member's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * The staff member's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * The staff member's email address.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The staff member's password.
     * Must contain at least 8 characters including uppercase, lowercase, digit, and special character.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    /**
     * The staff member's phone number.
     */
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    /**
     * The staff member's date of birth.
     * Must be in the past.
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * The staff member's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The date the staff member was hired.
     * Cannot be in the future.
     */
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;

    /**
     * The staff member's salary amount.
     */
    private BigDecimal salary;

    /**
     * The staff member's employment status.
     */
    @NotNull(message = "Employment status is required")
    private EmploymentStatus employeeStatus;

    /**
     * The department or section the staff member works in.
     */
    @NotBlank(message = "Department is required")
    private String department;

    /**
     * The staff member's role within the organization.
     */
    @NotNull(message = "Staff role is required")
    private StaffRole staffRole;

    /**
     * A description of the staff member's responsibilities.
     */
    private String responsibilities;
}