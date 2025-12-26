package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating a new medical record.
 * Contains clinical information documented during a patient appointment.
 */
@Data
public class CreateMedicalRecordRequestDTO {

    /**
     * The unique identifier of the patient.
     */
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    /**
     * The unique identifier of the doctor who created the record.
     */
    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    /**
     * The unique identifier of the associated appointment.
     */
    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;

    /**
     * The date and time the medical record was created.
     * Cannot be in the future.
     */
    @NotNull(message = "Record date is required")
    @PastOrPresent(message = "Record date cannot be in the future")
    private LocalDateTime recordDate;

    /**
     * The doctor's diagnosis based on the patient's examination.
     */
    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    /**
     * The symptoms reported by the patient.
     */
    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    /**
     * The recommended treatment plan.
     */
    @NotBlank(message = "Treatment is required")
    private String treatment;

    /**
     * Additional information about the disease or condition.
     */
    private String diseaseInfo;

    /**
     * The ICD (International Classification of Diseases) code for the diagnosis.
     */
    private String icdCode;
}