package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.MedicalRecord;
import it.fpili.happy_health_clinic.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository for accessing and managing Prescription entities.
 * Provides CRUD operations and custom queries for prescription data.
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    /**
     * Finds all prescriptions associated with a specific medical record.
     *
     * @param medicalRecord the medical record to search for
     * @return list of prescriptions linked to the medical record
     */
    List<Prescription> findByMedicalRecord(MedicalRecord medicalRecord);

    /**
     * Finds all prescriptions by drug name (case-insensitive partial match).
     *
     * @param drugName the drug name to search for
     * @return list of prescriptions containing the specified drug name
     */
    List<Prescription> findByDrugNameContainingIgnoreCase(String drugName);

    /**
     * Finds all active prescriptions for a patient on a specific date.
     * A prescription is active if the date falls between its start and end dates.
     *
     * @param patientId the unique identifier of the patient
     * @param date the date to check for active prescriptions
     * @return list of prescriptions active on the specified date
     */
    @Query("SELECT p FROM Prescription p WHERE p.medicalRecord.patient.id = :patientId " +
            "AND :date BETWEEN p.startDate AND p.endDate")
    List<Prescription> findActivePrescriptionsOnDate(@Param("patientId") UUID patientId,
                                                     @Param("date") LocalDate date);

    /**
     * Finds all prescriptions for a specific patient.
     *
     * @param patientId the unique identifier of the patient
     * @return list of prescriptions associated with the patient
     */
    @Query("SELECT p FROM Prescription p JOIN p.medicalRecord mr WHERE mr.patient.id = :patientId")
    List<Prescription> findByPatientId(@Param("patientId") UUID patientId);
}
