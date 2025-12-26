package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.repositories.DoctorRepository;
import it.fpili.happy_health_clinic.repositories.InvoiceRepository;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import it.fpili.happy_health_clinic.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

/**
 * Service for generating unique codes and identifiers for various entities.
 * Creates formatted codes for patients, employees, and invoices.
 */
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Repository for doctor data access.
     */
    private final DoctorRepository doctorRepository;

    /**
     * Repository for staff data access.
     */
    private final StaffRepository staffRepository;

    /**
     * Repository for invoice data access.
     */
    private final InvoiceRepository invoiceRepository;

    /**
     * Generates a unique patient code.
     * Format: PAT-XXXX (e.g., PAT-0001)
     *
     * @return a formatted patient code
     */
    public String generatePatientCode() {
        long count = patientRepository.count();
        return String.format("PAT-%04d", count + 1);
    }

    /**
     * Generates a unique employee code.
     * Format: EMP-XXXX (e.g., EMP-0001)
     * Combines count of doctors and staff members.
     *
     * @return a formatted employee code
     */
    public String generateEmployeeCode() {
        long doctorCount = doctorRepository.count();
        long staffCount = staffRepository.count();
        long totalEmployees = doctorCount + staffCount;
        return String.format("EMP-%04d", totalEmployees + 1);
    }

    /**
     * Generates a unique invoice number.
     * Format: INV-YYYY-XXXX (e.g., INV-2025-0001)
     *
     * @return a formatted invoice number with current year and sequence
     */
    public String generateInvoiceNumber() {
        int currentYear = Year.now().getValue();
        long countThisYear = invoiceRepository.count();
        return String.format("INV-%d-%04d", currentYear, countThisYear + 1);
    }
}
