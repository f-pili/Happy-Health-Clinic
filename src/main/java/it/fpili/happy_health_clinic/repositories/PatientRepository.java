package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing and managing Patient entities.
 * Provides CRUD operations and custom queries for patient data.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    /**
     * Finds a patient by their patient code.
     *
     * @param patientCode the patient code to search for
     * @return an Optional containing the patient if found, empty otherwise
     */
    Optional<Patient> findByPatientCode(String patientCode);

    /**
     * Finds a patient by their tax identification number.
     *
     * @param taxId the tax ID to search for
     * @return an Optional containing the patient if found, empty otherwise
     */
    Optional<Patient> findByTaxId(String taxId);

    /**
     * Checks whether a patient with the specified code exists.
     *
     * @param patientCode the patient code to check
     * @return true if a patient with the code exists, false otherwise
     */
    boolean existsByPatientCode(String patientCode);

    /**
     * Checks whether a patient with the specified tax ID exists.
     *
     * @param taxId the tax ID to check
     * @return true if a patient with the tax ID exists, false otherwise
     */
    boolean existsByTaxId(String taxId);
}
