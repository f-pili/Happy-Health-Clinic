package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for updating an existing appointment.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdateAppointmentRequestDTO {

    /**
     * The unique identifier of the doctor assigned to the appointment.
     */
    private UUID doctorId;

    /**
     * The updated scheduled date and time of the appointment.
     * Must be a future date if provided.
     */
    @Future(message = "Appointment must be in the future")
    private LocalDateTime appointmentDateTime;

    /**
     * The updated duration of the appointment in minutes.
     * Must be a positive number if provided.
     */
    @Positive(message = "Duration must be positive")
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
     * Additional notes or details about the appointment.
     */
    private String notes;
}
