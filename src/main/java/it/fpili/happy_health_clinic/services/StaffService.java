package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreateStaffRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateStaffRequestDTO;
import it.fpili.happy_health_clinic.dto.response.StaffResponseDTO;
import it.fpili.happy_health_clinic.entities.Staff;
import it.fpili.happy_health_clinic.enums.Role;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.StaffRepository;
import it.fpili.happy_health_clinic.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing staff members.
 * Handles business logic for creating, reading, updating, and deleting staff records.
 */
@Service
@RequiredArgsConstructor
public class StaffService {

    /**
     * Repository for staff data access.
     */
    private final StaffRepository staffRepository;

    /**
     * Repository for user data access.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for secure password hashing.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service for generating unique codes.
     */
    private final CodeGeneratorService codeGeneratorService;

    /**
     * Service for generating avatar URLs.
     */
    private final AvatarService avatarService;

    /**
     * Retrieves all staff members with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of staff members
     */
    public Page<StaffResponseDTO> getAllStaff(Pageable pageable) {
        return staffRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves a staff member by their ID.
     *
     * @param id the staff ID
     * @return the staff member
     * @throws ResourceNotFoundException if staff not found
     */
    public StaffResponseDTO getStaffById(UUID id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        return convertToDTO(staff);
    }

    /**
     * Retrieves all staff members with a specific role.
     *
     * @param staffRole the staff role to filter by
     * @return list of staff members with the specified role
     */
    public List<StaffResponseDTO> getStaffByRole(String staffRole) {
        return staffRepository.findByStaffRole(
                        it.fpili.happy_health_clinic.enums.StaffRole.valueOf(staffRole)
                )
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new staff member record.
     * Generates unique employee code and avatar URL.
     *
     * @param request the staff creation data
     * @return the created staff member
     * @throws BadRequestException if email already exists
     */
    @Transactional
    public StaffResponseDTO createStaff(CreateStaffRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Staff staff = new Staff();

        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmail(request.getEmail());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setDateOfBirth(request.getDateOfBirth());
        staff.setRole(Role.ADMIN);
        staff.setEmergencyContact(request.getEmergencyContact());

        staff.setAvatarUrl(avatarService.generateAvatarUrlWithCustomColor(
                request.getFirstName(),
                request.getLastName(),
                "DC2626"
        ));

        staff.setEmployeeCode(codeGeneratorService.generateEmployeeCode());
        staff.setHireDate(request.getHireDate());
        staff.setSalary(request.getSalary());
        staff.setEmployeeStatus(request.getEmployeeStatus());
        staff.setDepartment(request.getDepartment());

        staff.setStaffRole(request.getStaffRole());
        staff.setResponsibilities(request.getResponsibilities());

        Staff savedStaff = staffRepository.save(staff);
        return convertToDTO(savedStaff);
    }

    /**
     * Updates an existing staff member record.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the staff ID to update
     * @param request the updated staff data
     * @return the updated staff member
     * @throws ResourceNotFoundException if staff not found
     */
    @Transactional
    public StaffResponseDTO updateStaff(UUID id, UpdateStaffRequestDTO request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

        if (request.getFirstName() != null) {
            staff.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            staff.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            staff.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            staff.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmergencyContact() != null) {
            staff.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getSalary() != null) {
            staff.setSalary(request.getSalary());
        }
        if (request.getEmployeeStatus() != null) {
            staff.setEmployeeStatus(request.getEmployeeStatus());
        }
        if (request.getDepartment() != null) {
            staff.setDepartment(request.getDepartment());
        }
        if (request.getStaffRole() != null) {
            staff.setStaffRole(request.getStaffRole());
        }
        if (request.getResponsibilities() != null) {
            staff.setResponsibilities(request.getResponsibilities());
        }

        Staff updatedStaff = staffRepository.save(staff);
        return convertToDTO(updatedStaff);
    }

    /**
     * Deletes a staff member record.
     *
     * @param id the staff ID to delete
     * @throws ResourceNotFoundException if staff not found
     */
    @Transactional
    public void deleteStaff(UUID id) {
        if (!staffRepository.existsById(id)) {
            throw new ResourceNotFoundException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
    }

    /**
     * Converts a Staff entity to its DTO representation.
     *
     * @param staff the staff entity
     * @return the staff DTO
     */
    private StaffResponseDTO convertToDTO(Staff staff) {
        return new StaffResponseDTO(
                staff.getId(),
                staff.getFirstName(),
                staff.getLastName(),
                staff.getEmail(),
                staff.getPhoneNumber(),
                staff.getDateOfBirth(),
                staff.getAvatarUrl(),
                staff.getRole(),
                staff.getEmergencyContact(),
                staff.getEmployeeCode(),
                staff.getHireDate(),
                staff.getSalary(),
                staff.getEmployeeStatus(),
                staff.getDepartment(),
                staff.getStaffRole(),
                staff.getResponsibilities()
        );
    }
}