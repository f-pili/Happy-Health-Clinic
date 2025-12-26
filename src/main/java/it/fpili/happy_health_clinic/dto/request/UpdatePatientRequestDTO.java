package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.BloodType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for updating an existing patient record.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdatePatientRequestDTO {

    /**
     * The patient's first name.
     */
    private String firstName;

    /**
     * The patient's last name.
     */
    private String lastName;

    /**
     * The patient's email address.
     * Must be a valid email format if provided.
     */
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The patient's phone number.
     */
    private String phoneNumber;

    /**
     * The patient's date of birth.
     * Must be in the past if provided.
     */
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * The patient's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The patient's street address.
     */
    private String address;

    /**
     * The patient's city or municipality.
     */
    private String city;

    /**
     * The patient's province or region.
     */
    private String province;

    /**
     * The patient's postal code.
     */
    private String zipCode;

    /**
     * The patient's blood type.
     */
    private BloodType bloodType;

    /**
     * Known allergies of the patient.
     */
    private String allergies;

    /**
     * Chronic diseases or long-term medical conditions of the patient.
     */
    private String chronicDiseases;
}
