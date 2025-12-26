package it.fpili.happy_health_clinic.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a doctor.
 * Extends Employee with medical-specific information including license and specialization.
 */
@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends Employee {

    /**
     * The doctor's medical license number.
     * Must be unique across all doctors.
     */
    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    /**
     * The doctor's medical specialization (e.g., Cardiology, Pediatrics).
     */
    @Column(nullable = false, length = 100)
    private String specialization;

    /**
     * A biographical description of the doctor's qualifications and experience.
     */
    @Column(columnDefinition = "TEXT")
    private String biography;

    /**
     * List of appointments scheduled with this doctor.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * List of medical records created by this doctor.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
}