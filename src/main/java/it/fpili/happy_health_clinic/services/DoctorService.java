package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreateDoctorRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateDoctorRequestDTO;
import it.fpili.happy_health_clinic.dto.response.DoctorResponseDTO;
import it.fpili.happy_health_clinic.entities.Doctor;
import it.fpili.happy_health_clinic.enums.Role;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.DoctorRepository;
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
 * Service for managing doctors.
 * Handles business logic for creating, reading, updating, and deleting doctor records.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    /**
     * Repository for doctor data access.
     */
    private final DoctorRepository doctorRepository;

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
     * Retrieves all doctors with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of doctors
     */
    public Page<DoctorResponseDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves a doctor by their ID.
     *
     * @param id the doctor ID
     * @return the doctor
     * @throws ResourceNotFoundException if doctor not found
     */
    public DoctorResponseDTO getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return convertToDTO(doctor);
    }

    /**
     * Retrieves all doctors with a specific specialization.
     *
     * @param specialization the medical specialization to filter by
     * @return list of doctors with the specified specialization
     */
    public List<DoctorResponseDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new doctor record.
     * Generates unique employee code and avatar URL.
     *
     * @param request the doctor creation data
     * @return the created doctor
     * @throws BadRequestException if email or license number already exists
     */
    @Transactional
    public DoctorResponseDTO createDoctor(CreateDoctorRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BadRequestException("License number already exists");
        }

        Doctor doctor = new Doctor();

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setRole(Role.DOCTOR);
        doctor.setEmergencyContact(request.getEmergencyContact());

        doctor.setAvatarUrl(avatarService.generateAvatarUrlWithCustomColor(
                request.getFirstName(),
                request.getLastName(),
                "2563EB"
        ));

        doctor.setEmployeeCode(codeGeneratorService.generateEmployeeCode());
        doctor.setHireDate(request.getHireDate());
        doctor.setSalary(request.getSalary());
        doctor.setEmployeeStatus(request.getEmployeeStatus());
        doctor.setDepartment(request.getDepartment());

        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setBiography(request.getBiography());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDTO(savedDoctor);
    }

    /**
     * Updates an existing doctor record.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the doctor ID to update
     * @param request the updated doctor data
     * @return the updated doctor
     * @throws ResourceNotFoundException if doctor not found
     */
    @Transactional
    public DoctorResponseDTO updateDoctor(UUID id, UpdateDoctorRequestDTO request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        if (request.getFirstName() != null) {
            doctor.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            doctor.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            doctor.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            doctor.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmergencyContact() != null) {
            doctor.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getSalary() != null) {
            doctor.setSalary(request.getSalary());
        }
        if (request.getEmployeeStatus() != null) {
            doctor.setEmployeeStatus(request.getEmployeeStatus());
        }
        if (request.getDepartment() != null) {
            doctor.setDepartment(request.getDepartment());
        }
        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }
        if (request.getBiography() != null) {
            doctor.setBiography(request.getBiography());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return convertToDTO(updatedDoctor);
    }

    /**
     * Deletes a doctor record.
     *
     * @param id the doctor ID to delete
     * @throws ResourceNotFoundException if doctor not found
     */
    @Transactional
    public void deleteDoctor(UUID id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    /**
     * Converts a Doctor entity to its DTO representation.
     *
     * @param doctor the doctor entity
     * @return the doctor DTO
     */
    private DoctorResponseDTO convertToDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPhoneNumber(),
                doctor.getDateOfBirth(),
                doctor.getAvatarUrl(),
                doctor.getRole(),
                doctor.getEmergencyContact(),
                doctor.getEmployeeCode(),
                doctor.getHireDate(),
                doctor.getSalary(),
                doctor.getEmployeeStatus(),
                doctor.getDepartment(),
                doctor.getLicenseNumber(),
                doctor.getSpecialization(),
                doctor.getBiography()
        );
    }
}
