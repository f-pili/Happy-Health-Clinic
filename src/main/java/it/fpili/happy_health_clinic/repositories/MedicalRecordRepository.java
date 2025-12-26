package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Doctor;
import it.fpili.happy_health_clinic.entities.MedicalRecord;
import it.fpili.happy_health_clinic.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing and managing MedicalRecord entities.
 * Provides CRUD operations and custom queries for medical record data.
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    /**
     * Finds all medical records for a specific patient.
     *
     * @param patient the patient to search for
     * @return list of medical records associated with the patient
     */
    List<MedicalRecord> findByPatient(Patient patient);

    /**
     * Finds all medical records created by a specific doctor.
     *
     * @param doctor the doctor to search for
     * @return list of medical records created by the doctor
     */
    List<MedicalRecord> findByDoctor(Doctor doctor);

    /**
     * Finds a medical record by its associated appointment ID.
     *
     * @param appointmentId the appointment ID to search for
     * @return an Optional containing the medical record if found, empty otherwise
     */
    Optional<MedicalRecord> findByAppointmentId(UUID appointmentId);

    /**
     * Finds all medical records for a patient, ordered by record date in descending order.
     *
     * @param patient the patient to search for
     * @return list of medical records ordered by most recent first
     */
    List<MedicalRecord> findByPatientOrderByRecordDateDesc(Patient patient);

    /**
     * Finds all medical records created within a specified date and time range.
     *
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of medical records within the specified range
     */
    @Query("SELECT m FROM MedicalRecord m WHERE m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
