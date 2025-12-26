package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreatePrescriptionRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdatePrescriptionRequestDTO;
import it.fpili.happy_health_clinic.dto.response.PrescriptionResponseDTO;
import it.fpili.happy_health_clinic.entities.MedicalRecord;
import it.fpili.happy_health_clinic.entities.Prescription;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.MedicalRecordRepository;
import it.fpili.happy_health_clinic.repositories.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing prescriptions.
 * Handles business logic for creating, reading, updating, and deleting prescriptions.
 * Integrates with FDA API for drug information and sends email notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {

    /**
     * Repository for prescription data access.
     */
    private final PrescriptionRepository prescriptionRepository;

    /**
     * Repository for medical record data access.
     */
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Service for fetching drug information from FDA API.
     */
    private final DrugService drugService;

    /**
     * Service for sending email notifications.
     */
    private final EmailService emailService;

    /**
     * Retrieves all prescriptions with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of prescriptions
     */
    public Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves a prescription by its ID.
     *
     * @param id the prescription ID
     * @return the prescription
     * @throws ResourceNotFoundException if prescription not found
     */
    public PrescriptionResponseDTO getPrescriptionById(UUID id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return convertToDTO(prescription);
    }

    /**
     * Retrieves all prescriptions associated with a specific medical record.
     *
     * @param medicalRecordId the medical record ID
     * @return list of prescriptions for the medical record
     * @throws ResourceNotFoundException if medical record not found
     */
    public List<PrescriptionResponseDTO> getPrescriptionsByMedicalRecord(UUID medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + medicalRecordId));

        return prescriptionRepository.findByMedicalRecord(medicalRecord)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all prescriptions for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of prescriptions for the patient
     */
    public List<PrescriptionResponseDTO> getPrescriptionsByPatient(UUID patientId) {
        return prescriptionRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves active prescriptions for a patient on a specific date.
     *
     * @param patientId the patient ID
     * @param date the date to check for active prescriptions
     * @return list of prescriptions active on the specified date
     */
    public List<PrescriptionResponseDTO> getActivePrescriptions(UUID patientId, LocalDate date) {
        return prescriptionRepository.findActivePrescriptionsOnDate(patientId, date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches prescriptions by drug name (case-insensitive).
     *
     * @param drugName the name of the drug to search for
     * @return list of prescriptions containing the specified drug
     */
    public List<PrescriptionResponseDTO> searchByDrugName(String drugName) {
        return prescriptionRepository.findByDrugNameContainingIgnoreCase(drugName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new prescription.
     * Fetches drug information from FDA API and sends notification email.
     * End date is calculated from start date and duration if not provided.
     *
     * @param request the prescription creation data
     * @return the created prescription
     * @throws ResourceNotFoundException if medical record not found
     */
    @Transactional
    public PrescriptionResponseDTO createPrescription(CreatePrescriptionRequestDTO request) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + request.getMedicalRecordId()));

        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(medicalRecord);
        prescription.setDrugName(request.getDrugName());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDurationDays(request.getDurationDays());
        prescription.setStartDate(request.getStartDate());

        if (request.getEndDate() != null) {
            prescription.setEndDate(request.getEndDate());
        } else {
            prescription.setEndDate(request.getStartDate().plusDays(request.getDurationDays()));
        }

        prescription.setInstructions(request.getInstructions());

        try {
            log.info("Fetching drug information for: {}", request.getDrugName());
            DrugService.DrugInformation drugInfo = drugService.getDrugInformation(request.getDrugName());

            if (request.getDrugInfo() != null && !request.getDrugInfo().isBlank()) {
                prescription.setDrugInfo(request.getDrugInfo());
            } else {
                prescription.setDrugInfo(drugInfo.getDrugInfo());
            }

            if (request.getSideEffects() != null && !request.getSideEffects().isBlank()) {
                prescription.setSideEffects(request.getSideEffects());
            } else {
                prescription.setSideEffects(drugInfo.getSideEffects());
            }

            if (request.getContraindications() != null && !request.getContraindications().isBlank()) {
                prescription.setContraindications(request.getContraindications());
            } else {
                prescription.setContraindications(drugInfo.getContraindications());
            }

            log.info("Drug information populated successfully");
        } catch (Exception e) {
            log.error("Failed to fetch drug information: {}", e.getMessage());
            prescription.setDrugInfo(request.getDrugInfo());
            prescription.setSideEffects(request.getSideEffects());
            prescription.setContraindications(request.getContraindications());
        }

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        try {
            emailService.sendPrescriptionNotification(savedPrescription);
        } catch (Exception e) {
            log.error("Failed to send prescription notification email: {}", e.getMessage());
        }

        return convertToDTO(savedPrescription);
    }

    /**
     * Updates an existing prescription.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the prescription ID to update
     * @param request the updated prescription data
     * @return the updated prescription
     * @throws ResourceNotFoundException if prescription not found
     */
    @Transactional
    public PrescriptionResponseDTO updatePrescription(UUID id, UpdatePrescriptionRequestDTO request) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

        if (request.getDosage() != null) {
            prescription.setDosage(request.getDosage());
        }
        if (request.getFrequency() != null) {
            prescription.setFrequency(request.getFrequency());
        }
        if (request.getDurationDays() != null) {
            prescription.setDurationDays(request.getDurationDays());
        }
        if (request.getDrugInfo() != null) {
            prescription.setDrugInfo(request.getDrugInfo());
        }
        if (request.getSideEffects() != null) {
            prescription.setSideEffects(request.getSideEffects());
        }
        if (request.getContraindications() != null) {
            prescription.setContraindications(request.getContraindications());
        }
        if (request.getEndDate() != null) {
            prescription.setEndDate(request.getEndDate());
        }
        if (request.getInstructions() != null) {
            prescription.setInstructions(request.getInstructions());
        }

        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return convertToDTO(updatedPrescription);
    }

    /**
     * Deletes a prescription.
     *
     * @param id the prescription ID to delete
     * @throws ResourceNotFoundException if prescription not found
     */
    @Transactional
    public void deletePrescription(UUID id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prescription not found with id: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    /**
     * Converts a Prescription entity to its DTO representation.
     *
     * @param prescription the prescription entity
     * @return the prescription DTO
     */
    private PrescriptionResponseDTO convertToDTO(Prescription prescription) {
        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicalRecord().getId(),
                prescription.getMedicalRecord().getPatient().getId(),
                prescription.getMedicalRecord().getPatient().getFirstName() + " " +
                        prescription.getMedicalRecord().getPatient().getLastName(),
                prescription.getDrugName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDurationDays(),
                prescription.getDrugInfo(),
                prescription.getSideEffects(),
                prescription.getContraindications(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                prescription.getInstructions()
        );
    }
}