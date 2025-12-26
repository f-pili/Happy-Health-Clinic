package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import it.fpili.happy_health_clinic.enums.Role;
import it.fpili.happy_health_clinic.enums.StaffRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for staff response data.
 * Contains complete staff member information including personal, employment, and role details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponseDTO {

    /**
     * The unique identifier of the staff member.
     */
    private UUID id;

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
     */
    private String email;

    /**
     * The staff member's phone number.
     */
    private String phoneNumber;

    /**
     * The staff member's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * URL to the staff member's avatar image.
     */
    private String avatarUrl;

    /**
     * The staff member's role in the system.
     */
    private Role role;

    /**
     * The staff member's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The staff member's unique employee code.
     */
    private String employeeCode;

    /**
     * The date the staff member was hired.
     */
    private LocalDate hireDate;

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
