package it.fpili.happy_health_clinic.dto.request;

import lombok.Data;

/**
 * DTO for updating an existing medical record.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdateMedicalRecordRequestDTO {

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
