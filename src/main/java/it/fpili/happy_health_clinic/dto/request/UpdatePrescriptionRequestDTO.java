package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for updating an existing prescription.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdatePrescriptionRequestDTO {

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
     * Must be a positive number if provided.
     */
    @Positive(message = "Duration must be positive")
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
     * The date the prescription ends.
     */
    private LocalDate endDate;

    /**
     * Special instructions for taking the medication.
     */
    private String instructions;
}
