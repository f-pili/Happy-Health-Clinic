package it.fpili.happy_health_clinic.enums;

/**
 * Enumeration of possible appointment statuses.
 */
public enum AppointmentStatus {
    /**
     * Appointment is scheduled and awaiting completion.
     */
    SCHEDULED,

    /**
     * Appointment has been completed successfully.
     */
    COMPLETED,

    /**
     * Appointment has been cancelled.
     */
    CANCELLED,

    /**
     * Patient did not show up for the scheduled appointment.
     */
    NO_SHOW
}