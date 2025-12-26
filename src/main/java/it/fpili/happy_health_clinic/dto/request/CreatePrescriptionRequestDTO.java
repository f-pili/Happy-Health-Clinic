package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for creating a new prescription.
 * Contains medication details prescribed to a patient based on a medical record.
 */
@Data
public class CreatePrescriptionRequestDTO {

    /**
     * The unique identifier of the associated medical record.
     */
    @NotNull(message = "Medical record ID is required")
    private UUID medicalRecordId;

    /**
     * The name of the prescribed drug.
     */
    @NotBlank(message = "Drug name is required")
    private String drugName;

    /**
     * The prescribed dosage and unit of measurement.
     */
    @NotBlank(message = "Dosage is required")
    private String dosage;

    /**
     * How often the drug should be taken (e.g., twice daily, every 8 hours).
     */
    @NotBlank(message = "Frequency is required")
    private String frequency;

    /**
     * The duration of the prescription in days.
     * Must be a positive number.
     */
    @NotNull(message = "Duration in days is required")
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
     * The date the prescription becomes effective.
     * Cannot be in the future.
     */
    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    /**
     * The date the prescription ends.
     * Calculated based on start date and duration if not explicitly provided.
     */
    private LocalDate endDate;

    /**
     * Special instructions for taking the medication.
     */
    private String instructions;
}
