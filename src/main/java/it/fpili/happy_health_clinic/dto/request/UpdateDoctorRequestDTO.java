package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for updating an existing doctor record.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdateDoctorRequestDTO {

    /**
     * The doctor's first name.
     */
    private String firstName;

    /**
     * The doctor's last name.
     */
    private String lastName;

    /**
     * The doctor's email address.
     * Must be a valid email format if provided.
     */
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The doctor's phone number.
     */
    private String phoneNumber;

    /**
     * The doctor's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The doctor's salary amount.
     */
    private BigDecimal salary;

    /**
     * The doctor's employment status.
     */
    private EmploymentStatus employeeStatus;

    /**
     * The department or clinic section the doctor works in.
     */
    private String department;

    /**
     * The doctor's medical specialization.
     */
    private String specialization;

    /**
     * A biographical description of the doctor.
     */
    private String biography;
}