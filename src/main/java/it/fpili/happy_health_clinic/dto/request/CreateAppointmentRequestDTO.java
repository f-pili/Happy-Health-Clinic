package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating a new appointment.
 * Contains the required information to schedule a patient appointment with a doctor.
 */
@Data
public class CreateAppointmentRequestDTO {

    /**
     * The unique identifier of the patient.
     */
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    /**
     * The unique identifier of the doctor.
     */
    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    /**
     * The scheduled date and time of the appointment.
     * Must be a future date.
     */
    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment must be in the future")
    private LocalDateTime appointmentDateTime;

    /**
     * The duration of the appointment in minutes.
     * Defaults to 30 minutes if not specified.
     */
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes = 30;

    /**
     * The reason for the appointment.
     */
    @NotBlank(message = "Reason is required")
    private String reason;

    /**
     * Additional notes or details about the appointment.
     */
    private String notes;
}
