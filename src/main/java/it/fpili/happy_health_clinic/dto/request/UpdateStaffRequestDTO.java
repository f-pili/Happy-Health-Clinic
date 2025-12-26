package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import it.fpili.happy_health_clinic.enums.StaffRole;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for updating an existing staff member record.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdateStaffRequestDTO {

    /**
     * The staff member's first name.
     */
    private String firstName;

    /**
     * The staff member's last name.
     */
    private String lastName;

    /**
     * The staff member's email address.
     * Must be a valid email format if provided.
     */
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The staff member's phone number.
     */
    private String phoneNumber;

    /**
     * The staff member's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The staff member's salary amount.
     */
    private BigDecimal salary;

    /**
     * The staff member's employment status.
     */
    private EmploymentStatus employeeStatus;

    /**
     * The department or section the staff member works in.
     */
    private String department;

    /**
     * The staff member's role within the organization.
     */
    private StaffRole staffRole;

    /**
     * A description of the staff member's responsibilities.
     */
    private String responsibilities;
}
