package it.fpili.happy_health_clinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for prescription response data.
 * Contains complete prescription information including medication details and patient information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {

    /**
     * The unique identifier of the prescription.
     */
    private UUID id;

    /**
     * The unique identifier of the associated medical record.
     */
    private UUID medicalRecordId;

    /**
     * The unique identifier of the patient.
     */
    private UUID patientId;

    /**
     * The full name of the patient.
     */
    private String patientName;

    /**
     * The name of the prescribed drug.
     */
    private String drugName;

    /**
     * The prescribed dosage and unit of measurement.
     */
    private String dosage;

    /**
     * How often the drug should be taken (e.g., twice daily, every 8 hours).
     */
    private String frequency;

    /**
     * The duration of the prescription in days.
     */
    private Integer durationDays;

    /**
     * Additional pharmaceutical information about the drug.
     */
    private String drugInfo;

    /**
     * Potential side effects of the medication.
     */
    private String sideEffects;

    /**
     * Contraindications and warnings for the medication.
     */
    private String contraindications;

    /**
     * The date the prescription becomes effective.
     */
    private LocalDate startDate;

    /**
     * The date the prescription ends.
     */
    private LocalDate endDate;

    /**
     * Special instructions for taking the medication.
     */
    private String instructions;
}
