package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.UpdatePatientRequestDTO;
import it.fpili.happy_health_clinic.dto.response.PatientResponseDTO;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for managing patients.
 * Handles business logic for reading and updating patient records.
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Retrieves all patients with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of patients
     */
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id the patient ID
     * @return the patient
     * @throws ResourceNotFoundException if patient not found
     */
    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return convertToDTO(patient);
    }

    /**
     * Retrieves a patient by their patient code.
     *
     * @param patientCode the unique patient code
     * @return the patient
     * @throws ResourceNotFoundException if patient not found
     */
    public PatientResponseDTO getPatientByCode(String patientCode) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with code: " + patientCode));
        return convertToDTO(patient);
    }

    /**
     * Updates an existing patient record.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the patient ID to update
     * @param request the updated patient data
     * @return the updated patient
     * @throws ResourceNotFoundException if patient not found
     */
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            patient.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getEmergencyContact() != null) {
            patient.setEmergencyContact(request.getEmergencyContact());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            patient.setCity(request.getCity());
        }
        if (request.getProvince() != null) {
            patient.setProvince(request.getProvince());
        }
        if (request.getZipCode() != null) {
            patient.setZipCode(request.getZipCode());
        }
        if (request.getBloodType() != null) {
            patient.setBloodType(request.getBloodType());
        }
        if (request.getAllergies() != null) {
            patient.setAllergies(request.getAllergies());
        }
        if (request.getChronicDiseases() != null) {
            patient.setChronicDiseases(request.getChronicDiseases());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return convertToDTO(updatedPatient);
    }

    /**
     * Deletes a patient record.
     *
     * @param id the patient ID to delete
     * @throws ResourceNotFoundException if patient not found
     */
    @Transactional
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    /**
     * Converts a Patient entity to its DTO representation.
     *
     * @param patient the patient entity
     * @return the patient DTO
     */
    private PatientResponseDTO convertToDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getDateOfBirth(),
                patient.getAvatarUrl(),
                patient.getRole(),
                patient.getEmergencyContact(),
                patient.getPatientCode(),
                patient.getTaxId(),
                patient.getAddress(),
                patient.getCity(),
                patient.getProvince(),
                patient.getZipCode(),
                patient.getBloodType(),
                patient.getAllergies(),
                patient.getChronicDiseases()
        );
    }
}