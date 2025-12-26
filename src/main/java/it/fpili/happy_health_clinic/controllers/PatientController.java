package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.UpdatePatientRequestDTO;
import it.fpili.happy_health_clinic.dto.response.PatientResponseDTO;
import it.fpili.happy_health_clinic.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing patient information.
 * Provides endpoints for retrieving, updating, and deleting patient records.
 */
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * Retrieves all patients with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of patients
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<PatientResponseDTO>> getAllPatients(Pageable pageable) {
        Page<PatientResponseDTO> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id the unique identifier of the patient
     * @return the patient matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        PatientResponseDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    /**
     * Retrieves a patient by their patient code.
     *
     * @param patientCode the unique patient code
     * @return the patient matching the provided code
     */
    @GetMapping("/code/{patientCode}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<PatientResponseDTO> getPatientByCode(@PathVariable String patientCode) {
        PatientResponseDTO patient = patientService.getPatientByCode(patientCode);
        return ResponseEntity.ok(patient);
    }

    /**
     * Updates an existing patient record.
     *
     * @param id the unique identifier of the patient to update
     * @param request the updated patient data
     * @return the updated patient
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePatientRequestDTO request) {
        PatientResponseDTO updatedPatient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(updatedPatient);
    }

    /**
     * Deletes a patient record.
     *
     * @param id the unique identifier of the patient to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
