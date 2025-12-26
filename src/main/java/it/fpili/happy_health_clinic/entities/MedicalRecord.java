package it.fpili.happy_health_clinic.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a medical record.
 * Stores clinical information documented during a patient appointment.
 */
@Entity
@Table(name = "medical_records")
@Data
public class MedicalRecord {

    /**
     * The unique identifier of the medical record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The patient associated with this medical record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * The doctor who created this medical record.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * The appointment associated with this medical record.
     * One-to-one relationship.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    /**
     * The date and time the medical record was created.
     * Automatically set on entity creation and cannot be updated.
     */
    @CreationTimestamp
    @Column(name = "record_date", nullable = false, updatable = false)
    private LocalDateTime recordDate;

    /**
     * The doctor's diagnosis based on the patient's examination.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    /**
     * The symptoms reported by the patient.
     */
    @Column(columnDefinition = "TEXT")
    private String symptoms;

    /**
     * The recommended treatment plan.
     */
    @Column(columnDefinition = "TEXT")
    private String treatment;

    /**
     * Additional information about the disease or condition.
     */
    @Column(name = "disease_info", columnDefinition = "TEXT")
    private String diseaseInfo;

    /**
     * The ICD (International Classification of Diseases) code for the diagnosis.
     */
    @Column(name = "icd_code", length = 10)
    private String icdCode;

    /**
     * List of prescriptions issued based on this medical record.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions = new ArrayList<>();
}
