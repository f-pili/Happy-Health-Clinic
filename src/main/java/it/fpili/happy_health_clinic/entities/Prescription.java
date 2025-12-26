package it.fpili.happy_health_clinic.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing a prescription.
 * Stores medication details prescribed to a patient based on a medical record.
 */
@Entity
@Table(name = "prescriptions")
@Data
public class Prescription {

    /**
     * The unique identifier of the prescription.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The medical record associated with this prescription.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    /**
     * The name of the prescribed drug.
     */
    @Column(name = "drug_name", nullable = false, length = 255)
    private String drugName;

    /**
     * The prescribed dosage and unit of measurement.
     */
    @Column(nullable = false, length = 100)
    private String dosage;

    /**
     * How often the drug should be taken (e.g., twice daily, every 8 hours).
     */
    @Column(nullable = false, length = 100)
    private String frequency;

    /**
     * The duration of the prescription in days.
     */
    @Column(name = "duration_days")
    private Integer durationDays;

    /**
     * Additional pharmaceutical information about the drug.
     */
    @Column(name = "drug_info", columnDefinition = "TEXT")
    private String drugInfo;

    /**
     * Potential side effects of the medication.
     */
    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    /**
     * Contraindications and warnings for the medication.
     */
    @Column(columnDefinition = "TEXT")
    private String contraindications;

    /**
     * The date the prescription becomes effective.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * The date the prescription ends.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Special instructions for taking the medication.
     */
    @Column(columnDefinition = "TEXT")
    private String instructions;
}