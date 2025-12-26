package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreateDoctorRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateDoctorRequestDTO;
import it.fpili.happy_health_clinic.dto.response.DoctorResponseDTO;
import it.fpili.happy_health_clinic.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing doctors.
 * Provides endpoints for retrieving, creating, updating, and deleting doctor records.
 */
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Retrieves all doctors with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of doctors
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Page<DoctorResponseDTO>> getAllDoctors(Pageable pageable) {
        Page<DoctorResponseDTO> doctors = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Retrieves a doctor by their ID.
     *
     * @param id the unique identifier of the doctor
     * @return the doctor matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    /**
     * Retrieves all doctors by their specialization.
     *
     * @param specialization the medical specialization to filter by
     * @return list of doctors with the specified specialization
     */
    @GetMapping("/specialization/{specialization}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Creates a new doctor record.
     *
     * @param request the doctor creation data
     * @return the newly created doctor
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody CreateDoctorRequestDTO request) {
        DoctorResponseDTO doctor = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
    }

    /**
     * Updates an existing doctor record.
     *
     * @param id the unique identifier of the doctor to update
     * @param request the updated doctor data
     * @return the updated doctor
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDoctorRequestDTO request) {
        DoctorResponseDTO updatedDoctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(updatedDoctor);
    }

    /**
     * Deletes a doctor record.
     *
     * @param id the unique identifier of the doctor to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}