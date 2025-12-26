package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreateStaffRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateStaffRequestDTO;
import it.fpili.happy_health_clinic.dto.response.StaffResponseDTO;
import it.fpili.happy_health_clinic.services.StaffService;
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
 * REST controller for managing staff members.
 * Provides endpoints for retrieving, creating, updating, and deleting staff records.
 */
@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    /**
     * Retrieves all staff members with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of staff members
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<StaffResponseDTO>> getAllStaff(Pageable pageable) {
        Page<StaffResponseDTO> staff = staffService.getAllStaff(pageable);
        return ResponseEntity.ok(staff);
    }

    /**
     * Retrieves a staff member by their ID.
     *
     * @param id the unique identifier of the staff member
     * @return the staff member matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable UUID id) {
        StaffResponseDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    /**
     * Retrieves all staff members filtered by their role.
     *
     * @param staffRole the staff role to filter by
     * @return list of staff members with the specified role
     */
    @GetMapping("/role/{staffRole}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffResponseDTO>> getStaffByRole(@PathVariable String staffRole) {
        List<StaffResponseDTO> staff = staffService.getStaffByRole(staffRole);
        return ResponseEntity.ok(staff);
    }

    /**
     * Creates a new staff member record.
     *
     * @param request the staff creation data
     * @return the newly created staff member
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponseDTO> createStaff(@Valid @RequestBody CreateStaffRequestDTO request) {
        StaffResponseDTO staff = staffService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(staff);
    }

    /**
     * Updates an existing staff member record.
     *
     * @param id the unique identifier of the staff member to update
     * @param request the updated staff data
     * @return the updated staff member
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponseDTO> updateStaff(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStaffRequestDTO request) {
        StaffResponseDTO updatedStaff = staffService.updateStaff(id, request);
        return ResponseEntity.ok(updatedStaff);
    }

    /**
     * Deletes a staff member record.
     *
     * @param id the unique identifier of the staff member to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStaff(@PathVariable UUID id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
