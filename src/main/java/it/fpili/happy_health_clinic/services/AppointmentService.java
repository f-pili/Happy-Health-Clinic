package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreateAppointmentRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateAppointmentRequestDTO;
import it.fpili.happy_health_clinic.dto.response.AppointmentResponseDTO;
import it.fpili.happy_health_clinic.entities.Appointment;
import it.fpili.happy_health_clinic.entities.Doctor;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.AppointmentRepository;
import it.fpili.happy_health_clinic.repositories.DoctorRepository;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing appointments.
 * Handles business logic for creating, reading, updating, and deleting appointments.
 * Includes weather data integration and email notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    /**
     * Repository for appointment data access.
     */
    private final AppointmentRepository appointmentRepository;

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Repository for doctor data access.
     */
    private final DoctorRepository doctorRepository;

    /**
     * Service for fetching weather data.
     */
    private final WeatherService weatherService;

    /**
     * Service for sending email notifications.
     */
    private final EmailService emailService;

    /**
     * Retrieves all appointments with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of appointments
     */
    public Page<AppointmentResponseDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id the appointment ID
     * @return the appointment
     * @throws ResourceNotFoundException if appointment not found
     */
    public AppointmentResponseDTO getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return convertToDTO(appointment);
    }

    /**
     * Retrieves all appointments for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of appointments for the patient
     * @throws ResourceNotFoundException if patient not found
     */
    public List<AppointmentResponseDTO> getAppointmentsByPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return appointmentRepository.findByPatient(patient)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all appointments assigned to a specific doctor.
     *
     * @param doctorId the doctor ID
     * @return list of appointments for the doctor
     * @throws ResourceNotFoundException if doctor not found
     */
    public List<AppointmentResponseDTO> getAppointmentsByDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        return appointmentRepository.findByDoctor(doctor)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves appointments filtered by status.
     *
     * @param status the appointment status to filter by
     * @return list of appointments with the specified status
     */
    public List<AppointmentResponseDTO> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves appointments within a specified date and time range.
     *
     * @param start the start date and time
     * @param end the end date and time
     * @return list of appointments within the range
     */
    public List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDateRange(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new appointment.
     * Validates doctor availability, fetches weather data, and sends confirmation email.
     *
     * @param request the appointment creation data
     * @return the created appointment
     * @throws ResourceNotFoundException if patient or doctor not found
     * @throws BadRequestException if doctor is not available at requested time
     */
    @Transactional
    public AppointmentResponseDTO createAppointment(CreateAppointmentRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        List<Appointment> conflictingAppointments = appointmentRepository.findByDoctorAndDateRange(
                doctor,
                request.getAppointmentDateTime(),
                request.getAppointmentDateTime().plusMinutes(request.getDurationMinutes())
        );

        if (!conflictingAppointments.isEmpty()) {
            throw new BadRequestException("Doctor is not available at the requested time");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setCreatedAt(LocalDateTime.now());

        try {
            log.info("Fetching weather data for patient city: {}", patient.getCity());
            WeatherService.WeatherData weather = weatherService.getWeatherForCity(patient.getCity());

            appointment.setWeatherConditions(weather.getConditions());
            appointment.setTemperature(weather.getTemperature());
            appointment.setWeatherAlert(weather.getAlert());

            log.info("Weather data saved - Temp: {}°C, Alert: {}",
                    weather.getTemperature(),
                    weather.getAlert() != null ? weather.getAlert() : "None");
        } catch (Exception e) {
            log.error("Failed to fetch weather data: {}", e.getMessage());
            appointment.setWeatherConditions("Weather data unavailable");
            appointment.setTemperature(BigDecimal.valueOf(20.0));
            appointment.setWeatherAlert(null);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        try {
            emailService.sendAppointmentConfirmation(savedAppointment);
        } catch (Exception e) {
            log.error("Failed to send appointment confirmation email: {}", e.getMessage());
        }

        return convertToDTO(savedAppointment);
    }

    /**
     * Updates an existing appointment.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the appointment ID to update
     * @param request the updated appointment data
     * @return the updated appointment
     * @throws ResourceNotFoundException if appointment or related entity not found
     */
    @Transactional
    public AppointmentResponseDTO updateAppointment(UUID id, UpdateAppointmentRequestDTO request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (request.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
            appointment.setDoctor(doctor);
        }

        if (request.getAppointmentDateTime() != null) {
            appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        }

        if (request.getDurationMinutes() != null) {
            appointment.setDurationMinutes(request.getDurationMinutes());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }

        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(updatedAppointment);
    }

    /**
     * Deletes an appointment.
     *
     * @param id the appointment ID to delete
     * @throws ResourceNotFoundException if appointment not found
     */
    @Transactional
    public void deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    /**
     * Converts an Appointment entity to its DTO representation.
     *
     * @param appointment the appointment entity
     * @return the appointment DTO
     */
    private AppointmentResponseDTO convertToDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                appointment.getPatient().getPatientCode(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                appointment.getDoctor().getSpecialization(),
                appointment.getAppointmentDateTime(),
                appointment.getDurationMinutes(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getNotes(),
                appointment.getWeatherConditions(),
                appointment.getTemperature(),
                appointment.getWeatherAlert(),
                appointment.getCreatedAt()
        );
    }
}
