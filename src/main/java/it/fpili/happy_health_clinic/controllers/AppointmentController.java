package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreateAppointmentRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateAppointmentRequestDTO;
import it.fpili.happy_health_clinic.dto.response.AppointmentResponseDTO;
import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import it.fpili.happy_health_clinic.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing appointments.
 * Provides endpoints for creating, retrieving, updating, and deleting appointments.
 */
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Retrieves all appointments with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated appointments
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAllAppointments(Pageable pageable) {
        Page<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments(pageable);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id the unique identifier of the appointment
     * @return the appointment matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponseDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    /**
     * Retrieves all appointments for a specific patient.
     *
     * @param patientId the patient's unique identifier
     * @return list of appointments for the patient
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable UUID patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves all appointments for a specific doctor.
     *
     * @param doctorId the doctor's unique identifier
     * @return list of appointments for the doctor
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves appointments filtered by status.
     *
     * @param status the appointment status to filter by
     * @return list of appointments with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Retrieves appointments within a specified date range.
     *
     * @param start the start date of the range (ISO 8601 format)
     * @param end the end date of the range (ISO 8601 format)
     * @return list of appointments within the specified range
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDateRange(start, end);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Creates a new appointment.
     *
     * @param request the appointment creation data
     * @return the newly created appointment
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody CreateAppointmentRequestDTO request) {
        AppointmentResponseDTO appointment = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    /**
     * Updates an existing appointment.
     *
     * @param id the unique identifier of the appointment to update
     * @param request the updated appointment data
     * @return the updated appointment
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentRequestDTO request) {
        AppointmentResponseDTO updatedAppointment = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(updatedAppointment);
    }

    /**
     * Deletes an appointment.
     *
     * @param id the unique identifier of the appointment to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}