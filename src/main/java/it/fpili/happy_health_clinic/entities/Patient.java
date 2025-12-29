package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.BloodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a patient.
 * Extends User with patient-specific information including medical history and contact details.
 */
@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends User {

    /**
     * The patient's unique code identifier.
     * Must be unique across all patients.
     */
    @Column(name = "patient_code", nullable = false, unique = true, length = 50)
    private String patientCode;

    /**
     * The patient's tax identification number.
     * Must be unique across all patients.
     */
    @Column(name = "tax_id", nullable = false, unique = true, length = 16)
    private String taxId;

    /**
     * The patient's street address.
     */
    @Column(nullable = false)
    private String address;

    /**
     * The patient's city or municipality.
     */
    @Column(nullable = false, length = 100)
    private String city;

    /**
     * The patient's province or region.
     */
    @Column(nullable = false, length = 50)
    private String province;

    /**
     * The patient's postal code.
     */
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    /**
     * The patient's blood type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", length = 10)
    private BloodType bloodType;

    /**
     * Known allergies of the patient.
     */
    @Column(columnDefinition = "TEXT")
    private String allergies;

    /**
     * Chronic diseases or long-term medical conditions of the patient.
     */
    @Column(name = "chronic_diseases", columnDefinition = "TEXT")
    private String chronicDiseases;

    /**
     * List of appointments scheduled for this patient.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * List of medical records created for this patient.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    /**
     * List of invoices generated for this patient.
     * One-to-many relationship with cascading delete and orphan removal.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();
}