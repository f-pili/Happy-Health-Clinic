package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for appointment response data.
 * Contains complete appointment information including patient and doctor details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    /**
     * The unique identifier of the appointment.
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
     * The unique identifier of the doctor.
     */
    private UUID doctorId;

    /**
     * The full name of the doctor.
     */
    private String doctorName;

    /**
     * The medical specialization of the doctor.
     */
    private String doctorSpecialization;

    /**
     * The scheduled date and time of the appointment.
     */
    private LocalDateTime appointmentDateTime;

    /**
     * The duration of the appointment in minutes.
     */
    private Integer durationMinutes;

    /**
     * The current status of the appointment.
     */
    private AppointmentStatus status;

    /**
     * The reason for the appointment.
     */
    private String reason;

    /**
     * Additional notes about the appointment.
     */
    private String notes;

    /**
     * Weather conditions at the time of the appointment.
     */
    private String weatherConditions;

    /**
     * The temperature at the time of the appointment.
     */
    private BigDecimal temperature;

    /**
     * Any weather-related alerts or warnings.
     */
    private String weatherAlert;

    /**
     * The timestamp when the appointment record was created.
     */
    private LocalDateTime createdAt;
}
