package it.fpili.happy_health_clinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for medical record response data.
 * Contains complete medical record information including patient, doctor, and clinical details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponseDTO {

    /**
     * The unique identifier of the medical record.
     */
    private UUID id;

    /**
     * The unique identifier of the patient.
     */
    private UUID patientId;

    /**
     * The full name of the patient.
     */
    private String patientName;

    /**
     * The patient code for quick identification.
     */
    private String patientCode;

    /**
     * The unique identifier of the doctor who created the record.
     */
    private UUID doctorId;

    /**
     * The full name of the doctor.
     */
    private String doctorName;

    /**
     * The unique identifier of the associated appointment.
     */
    private UUID appointmentId;

    /**
     * The date and time the medical record was created.
     */
    private LocalDateTime recordDate;

    /**
     * The doctor's diagnosis based on the patient's examination.
     */
    private String diagnosis;

    /**
     * The symptoms reported by the patient.
     */
    private String symptoms;

    /**
     * The recommended treatment plan.
     */
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
