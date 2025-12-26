package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a medical appointment.
 * Stores information about scheduled appointments between patients and doctors.
 */
@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    /**
     * The unique identifier of the appointment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The patient associated with this appointment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * The doctor assigned to this appointment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * The scheduled date and time of the appointment.
     */
    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    /**
     * The duration of the appointment in minutes.
     * Defaults to 30 minutes.
     */
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 30;

    /**
     * The current status of the appointment.
     * Defaults to SCHEDULED.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    /**
     * The reason for the appointment.
     */
    @Column(nullable = false, length = 255)
    private String reason;

    /**
     * Additional notes about the appointment.
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Weather conditions at the time of the appointment.
     */
    @Column(name = "weather_conditions", length = 100)
    private String weatherConditions;

    /**
     * The temperature at the time of the appointment.
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    /**
     * Any weather-related alerts or warnings.
     */
    @Column(name = "weather_alert", columnDefinition = "TEXT")
    private String weatherAlert;

    /**
     * The timestamp when the appointment record was created.
     * Automatically set on entity creation.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Sets the creation timestamp before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * The medical record associated with this appointment.
     * One-to-one relationship with cascading delete.
     */
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private MedicalRecord medicalRecord;

    /**
     * The invoice associated with this appointment.
     * One-to-one relationship with cascading delete.
     */
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Invoice invoice;
}
