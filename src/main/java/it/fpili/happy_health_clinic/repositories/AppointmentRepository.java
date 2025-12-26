package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Appointment;
import it.fpili.happy_health_clinic.entities.Doctor;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for accessing and managing Appointment entities.
 * Provides CRUD operations and custom queries for appointment data.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    /**
     * Finds all appointments for a specific patient.
     *
     * @param patient the patient to search for
     * @return list of appointments associated with the patient
     */
    List<Appointment> findByPatient(Patient patient);

    /**
     * Finds all appointments assigned to a specific doctor.
     *
     * @param doctor the doctor to search for
     * @return list of appointments assigned to the doctor
     */
    List<Appointment> findByDoctor(Doctor doctor);

    /**
     * Finds all appointments with a specific status.
     *
     * @param status the appointment status to filter by
     * @return list of appointments with the specified status
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Finds all appointments for a patient with a specific status.
     *
     * @param patient the patient to search for
     * @param status the appointment status to filter by
     * @return list of appointments matching both criteria
     */
    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

    /**
     * Finds all appointments assigned to a doctor with a specific status.
     *
     * @param doctor the doctor to search for
     * @param status the appointment status to filter by
     * @return list of appointments matching both criteria
     */
    List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);

    /**
     * Finds all appointments within a specified date and time range.
     *
     * @param start the start of the date range
     * @param end the end of the date range
     * @return list of appointments within the specified range
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * Finds all appointments for a specific doctor within a date and time range.
     *
     * @param doctor the doctor to search for
     * @param start the start of the date range
     * @param end the end of the date range
     * @return list of appointments for the doctor within the specified range
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorAndDateRange(
            @Param("doctor") Doctor doctor,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}