package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.BloodType;
import it.fpili.happy_health_clinic.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for patient response data.
 * Contains complete patient information including personal, contact, and medical details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {

    /**
     * The unique identifier of the patient.
     */
    private UUID id;

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
     */
    private String email;

    /**
     * The patient's phone number.
     */
    private String phoneNumber;

    /**
     * The patient's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * URL to the patient's avatar image.
     */
    private String avatarUrl;

    /**
     * The patient's role in the system.
     */
    private Role role;

    /**
     * The patient's emergency contact information.
     */
    private String emergencyContact;

    /**
     * The unique patient code for quick identification.
     */
    private String patientCode;

    /**
     * The patient's tax identification number.
     */
    private String taxId;

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
