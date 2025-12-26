package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing and managing Doctor entities.
 * Provides CRUD operations and custom queries for doctor data.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    /**
     * Finds a doctor by their medical license number.
     *
     * @param licenseNumber the license number to search for
     * @return an Optional containing the doctor if found, empty otherwise
     */
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    /**
     * Checks whether a doctor with the specified license number exists.
     *
     * @param licenseNumber the license number to check
     * @return true if a doctor with the license number exists, false otherwise
     */
    boolean existsByLicenseNumber(String licenseNumber);

    /**
     * Finds all doctors with a specific specialization.
     *
     * @param specialization the medical specialization to filter by
     * @return list of doctors with the specified specialization
     */
    List<Doctor> findBySpecialization(String specialization);
}