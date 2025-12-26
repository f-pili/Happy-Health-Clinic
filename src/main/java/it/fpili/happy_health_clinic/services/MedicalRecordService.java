package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreateMedicalRecordRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateMedicalRecordRequestDTO;
import it.fpili.happy_health_clinic.dto.response.MedicalRecordResponseDTO;
import it.fpili.happy_health_clinic.entities.Appointment;
import it.fpili.happy_health_clinic.entities.Doctor;
import it.fpili.happy_health_clinic.entities.MedicalRecord;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.AppointmentRepository;
import it.fpili.happy_health_clinic.repositories.DoctorRepository;
import it.fpili.happy_health_clinic.repositories.MedicalRecordRepository;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing medical records.
 * Handles business logic for creating, reading, updating, and deleting medical records.
 */
@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    /**
     * Repository for medical record data access.
     */
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Repository for doctor data access.
     */
    private final DoctorRepository doctorRepository;

    /**
     * Repository for appointment data access.
     */
    private final AppointmentRepository appointmentRepository;

    /**
     * Retrieves all medical records with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of medical records
     */
    public Page<MedicalRecordResponseDTO> getAllMedicalRecords(Pageable pageable) {
        return medicalRecordRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves a medical record by its ID.
     *
     * @param id the medical record ID
     * @return the medical record
     * @throws ResourceNotFoundException if medical record not found
     */
    public MedicalRecordResponseDTO getMedicalRecordById(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
        return convertToDTO(medicalRecord);
    }

    /**
     * Retrieves all medical records for a specific patient, ordered by most recent first.
     *
     * @param patientId the patient ID
     * @return list of medical records for the patient
     * @throws ResourceNotFoundException if patient not found
     */
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return medicalRecordRepository.findByPatientOrderByRecordDateDesc(patient)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all medical records created by a specific doctor.
     *
     * @param doctorId the doctor ID
     * @return list of medical records created by the doctor
     * @throws ResourceNotFoundException if doctor not found
     */
    public List<MedicalRecordResponseDTO> getMedicalRecordsByDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        return medicalRecordRepository.findByDoctor(doctor)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves medical records created within a specified date and time range.
     *
     * @param start the start date and time
     * @param end the end date and time
     * @return list of medical records within the range
     */
    public List<MedicalRecordResponseDTO> getMedicalRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return medicalRecordRepository.findByDateRange(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new medical record.
     * Validates that no medical record already exists for the appointment.
     *
     * @param request the medical record creation data
     * @return the created medical record
     * @throws ResourceNotFoundException if patient, doctor, or appointment not found
     * @throws BadRequestException if medical record already exists for the appointment
     */
    @Transactional
    public MedicalRecordResponseDTO createMedicalRecord(CreateMedicalRecordRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));

        if (medicalRecordRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new BadRequestException("Medical record already exists for this appointment");
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setDoctor(doctor);
        medicalRecord.setAppointment(appointment);
        medicalRecord.setRecordDate(request.getRecordDate());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setSymptoms(request.getSymptoms());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setDiseaseInfo(request.getDiseaseInfo());
        medicalRecord.setIcdCode(request.getIcdCode());

        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        return convertToDTO(savedRecord);
    }

    /**
     * Updates an existing medical record.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the medical record ID to update
     * @param request the updated medical record data
     * @return the updated medical record
     * @throws ResourceNotFoundException if medical record not found
     */
    @Transactional
    public MedicalRecordResponseDTO updateMedicalRecord(UUID id, UpdateMedicalRecordRequestDTO request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));

        if (request.getDiagnosis() != null) {
            medicalRecord.setDiagnosis(request.getDiagnosis());
        }
        if (request.getSymptoms() != null) {
            medicalRecord.setSymptoms(request.getSymptoms());
        }
        if (request.getTreatment() != null) {
            medicalRecord.setTreatment(request.getTreatment());
        }
        if (request.getDiseaseInfo() != null) {
            medicalRecord.setDiseaseInfo(request.getDiseaseInfo());
        }
        if (request.getIcdCode() != null) {
            medicalRecord.setIcdCode(request.getIcdCode());
        }

        MedicalRecord updatedRecord = medicalRecordRepository.save(medicalRecord);
        return convertToDTO(updatedRecord);
    }

    /**
     * Deletes a medical record.
     *
     * @param id the medical record ID to delete
     * @throws ResourceNotFoundException if medical record not found
     */
    @Transactional
    public void deleteMedicalRecord(UUID id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medical record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    /**
     * Converts a MedicalRecord entity to its DTO representation.
     *
     * @param medicalRecord the medical record entity
     * @return the medical record DTO
     */
    private MedicalRecordResponseDTO convertToDTO(MedicalRecord medicalRecord) {
        return new MedicalRecordResponseDTO(
                medicalRecord.getId(),
                medicalRecord.getPatient().getId(),
                medicalRecord.getPatient().getFirstName() + " " + medicalRecord.getPatient().getLastName(),
                medicalRecord.getPatient().getPatientCode(),
                medicalRecord.getDoctor().getId(),
                medicalRecord.getDoctor().getFirstName() + " " + medicalRecord.getDoctor().getLastName(),
                medicalRecord.getAppointment().getId(),
                medicalRecord.getRecordDate(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getSymptoms(),
                medicalRecord.getTreatment(),
                medicalRecord.getDiseaseInfo(),
                medicalRecord.getIcdCode()
        );
    }
}
