package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import it.fpili.happy_health_clinic.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for doctor response data.
 * Contains complete doctor information including personal, employment, and professional details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {

    /**
     * The unique identifier of the doctor.
     */
    private UUID id;

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
     */
    private String email;

    /**
     * The doctor's phone number.
     */
    private String phoneNumber;

    /**
     * The doctor's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * URL to the doctor's avatar image.
     */
    private String avatarUrl;

    /**
     * The doctor's role in the system.
     */
    private Role role;

    /**
     * The doctor's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The doctor's unique employee code.
     */
    private String employeeCode;

    /**
     * The date the doctor was hired.
     */
    private LocalDate hireDate;

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
     * The doctor's medical license number.
     */
    private String licenseNumber;

    /**
     * The doctor's medical specialization.
     */
    private String specialization;

    /**
     * A biographical description of the doctor.
     */
    private String biography;
}
