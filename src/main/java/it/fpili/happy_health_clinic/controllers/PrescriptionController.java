package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreatePrescriptionRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdatePrescriptionRequestDTO;
import it.fpili.happy_health_clinic.dto.response.PrescriptionResponseDTO;
import it.fpili.happy_health_clinic.services.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing prescriptions.
 * Provides endpoints for retrieving, creating, updating, and deleting prescription records.
 */
@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * Retrieves all prescriptions with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of prescriptions
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<PrescriptionResponseDTO>> getAllPrescriptions(Pageable pageable) {
        Page<PrescriptionResponseDTO> prescriptions = prescriptionService.getAllPrescriptions(pageable);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Retrieves a prescription by its ID.
     *
     * @param id the unique identifier of the prescription
     * @return the prescription matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> getPrescriptionById(@PathVariable UUID id) {
        PrescriptionResponseDTO prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    /**
     * Retrieves all prescriptions associated with a specific medical record.
     *
     * @param medicalRecordId the unique identifier of the medical record
     * @return list of prescriptions for the medical record
     */
    @GetMapping("/medical-record/{medicalRecordId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByMedicalRecord(@PathVariable UUID medicalRecordId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByMedicalRecord(medicalRecordId);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Retrieves all prescriptions for a specific patient.
     *
     * @param patientId the unique identifier of the patient
     * @return list of prescriptions for the patient
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByPatient(@PathVariable UUID patientId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Retrieves active prescriptions for a patient on a specific date.
     *
     * @param patientId the unique identifier of the patient
     * @param date the reference date to determine active status (ISO 8601 format)
     * @return list of prescriptions active on the specified date
     */
    @GetMapping("/patient/{patientId}/active")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getActivePrescriptions(
            @PathVariable UUID patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getActivePrescriptions(patientId, date);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Searches prescriptions by drug name.
     *
     * @param drugName the name of the drug to search for
     * @return list of prescriptions containing the specified drug
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> searchByDrugName(@RequestParam String drugName) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.searchByDrugName(drugName);
        return ResponseEntity.ok(prescriptions);
    }

    /**
     * Creates a new prescription.
     *
     * @param request the prescription creation data
     * @return the newly created prescription
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(@Valid @RequestBody CreatePrescriptionRequestDTO request) {
        PrescriptionResponseDTO prescription = prescriptionService.createPrescription(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescription);
    }

    /**
     * Updates an existing prescription.
     *
     * @param id the unique identifier of the prescription to update
     * @param request the updated prescription data
     * @return the updated prescription
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> updatePrescription(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePrescriptionRequestDTO request) {
        PrescriptionResponseDTO updatedPrescription = prescriptionService.updatePrescription(id, request);
        return ResponseEntity.ok(updatedPrescription);
    }

    /**
     * Deletes a prescription.
     *
     * @param id the unique identifier of the prescription to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePrescription(@PathVariable UUID id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
