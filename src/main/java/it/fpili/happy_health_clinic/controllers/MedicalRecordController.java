package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreateMedicalRecordRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateMedicalRecordRequestDTO;
import it.fpili.happy_health_clinic.dto.response.MedicalRecordResponseDTO;
import it.fpili.happy_health_clinic.services.MedicalRecordService;
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
 * REST controller for managing medical records.
 * Provides endpoints for retrieving, creating, updating, and deleting medical record entries.
 */
@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Retrieves all medical records with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of medical records
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<MedicalRecordResponseDTO>> getAllMedicalRecords(Pageable pageable) {
        Page<MedicalRecordResponseDTO> records = medicalRecordService.getAllMedicalRecords(pageable);
        return ResponseEntity.ok(records);
    }

    /**
     * Retrieves a medical record by its ID.
     *
     * @param id the unique identifier of the medical record
     * @return the medical record matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(@PathVariable UUID id) {
        MedicalRecordResponseDTO record = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(record);
    }

    /**
     * Retrieves all medical records for a specific patient.
     *
     * @param patientId the unique identifier of the patient
     * @return list of medical records for the patient
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(@PathVariable UUID patientId) {
        List<MedicalRecordResponseDTO> records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        return ResponseEntity.ok(records);
    }

    /**
     * Retrieves all medical records created by a specific doctor.
     *
     * @param doctorId the unique identifier of the doctor
     * @return list of medical records created by the doctor
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByDoctor(@PathVariable UUID doctorId) {
        List<MedicalRecordResponseDTO> records = medicalRecordService.getMedicalRecordsByDoctor(doctorId);
        return ResponseEntity.ok(records);
    }

    /**
     * Retrieves medical records within a specified date range.
     *
     * @param start the start date of the range (ISO 8601 format)
     * @param end the end date of the range (ISO 8601 format)
     * @return list of medical records within the specified range
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<MedicalRecordResponseDTO> records = medicalRecordService.getMedicalRecordsByDateRange(start, end);
        return ResponseEntity.ok(records);
    }

    /**
     * Creates a new medical record.
     *
     * @param request the medical record creation data
     * @return the newly created medical record
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequestDTO request) {
        MedicalRecordResponseDTO record = medicalRecordService.createMedicalRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    /**
     * Updates an existing medical record.
     *
     * @param id the unique identifier of the medical record to update
     * @param request the updated medical record data
     * @return the updated medical record
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMedicalRecordRequestDTO request) {
        MedicalRecordResponseDTO updatedRecord = medicalRecordService.updateMedicalRecord(id, request);
        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * Deletes a medical record.
     *
     * @param id the unique identifier of the medical record to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}