package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.CreateInvoiceRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateInvoiceRequestDTO;
import it.fpili.happy_health_clinic.dto.response.InvoiceResponseDTO;
import it.fpili.happy_health_clinic.entities.Appointment;
import it.fpili.happy_health_clinic.entities.Invoice;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.AppointmentRepository;
import it.fpili.happy_health_clinic.repositories.InvoiceRepository;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing invoices.
 * Handles business logic for creating, reading, updating, and deleting invoices.
 * Includes payment tracking and email notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    /**
     * Repository for invoice data access.
     */
    private final InvoiceRepository invoiceRepository;

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Repository for appointment data access.
     */
    private final AppointmentRepository appointmentRepository;

    /**
     * Service for generating unique codes.
     */
    private final CodeGeneratorService codeGeneratorService;

    /**
     * Service for sending email notifications.
     */
    private final EmailService emailService;

    /**
     * Retrieves all invoices with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of invoices
     */
    public Page<InvoiceResponseDTO> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the invoice ID
     * @return the invoice
     * @throws ResourceNotFoundException if invoice not found
     */
    public InvoiceResponseDTO getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return convertToDTO(invoice);
    }

    /**
     * Retrieves an invoice by its invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return the invoice
     * @throws ResourceNotFoundException if invoice not found
     */
    public InvoiceResponseDTO getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));
        return convertToDTO(invoice);
    }

    /**
     * Retrieves all invoices for a specific patient.
     *
     * @param patientId the patient ID
     * @return list of invoices for the patient
     * @throws ResourceNotFoundException if patient not found
     */
    public List<InvoiceResponseDTO> getInvoicesByPatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return invoiceRepository.findByPatient(patient)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves invoices filtered by status.
     *
     * @param status the invoice status to filter by
     * @return list of invoices with the specified status
     */
    public List<InvoiceResponseDTO> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all overdue invoices (pending invoices with past due dates).
     *
     * @param currentDate the reference date for determining overdue status
     * @return list of overdue invoices
     */
    public List<InvoiceResponseDTO> getOverdueInvoices(LocalDate currentDate) {
        return invoiceRepository.findOverdueInvoices(currentDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total amount of invoices grouped by status.
     *
     * @param status the invoice status to calculate total for
     * @return the total amount for invoices with the specified status
     */
    public BigDecimal calculateTotalByStatus(InvoiceStatus status) {
        return invoiceRepository.calculateTotalByStatus(status);
    }

    /**
     * Creates a new invoice.
     * Generates unique invoice number and sends notification email.
     *
     * @param request the invoice creation data
     * @return the created invoice
     * @throws ResourceNotFoundException if patient or appointment not found
     */
    @Transactional
    public InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));

        Invoice invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setAppointment(appointment);
        invoice.setInvoiceNumber(codeGeneratorService.generateInvoiceNumber());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setAmount(request.getAmount());
        invoice.setTaxAmount(request.getTaxAmount());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setDescription(request.getDescription());

        Invoice savedInvoice = invoiceRepository.save(invoice);

        try {
            emailService.sendInvoiceNotification(savedInvoice);
        } catch (Exception e) {
            log.warn("Could not send invoice email to {}: {}", patient.getEmail(), e.getMessage());
        }

        return convertToDTO(savedInvoice);
    }

    /**
     * Updates an existing invoice.
     * All fields are optional and only provided fields are updated.
     *
     * @param id the invoice ID to update
     * @param request the updated invoice data
     * @return the updated invoice
     * @throws ResourceNotFoundException if invoice not found
     */
    @Transactional
    public InvoiceResponseDTO updateInvoice(UUID id, UpdateInvoiceRequestDTO request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }
        if (request.getAmount() != null) {
            invoice.setAmount(request.getAmount());
        }
        if (request.getTaxAmount() != null) {
            invoice.setTaxAmount(request.getTaxAmount());
        }
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        if (request.getDescription() != null) {
            invoice.setDescription(request.getDescription());
        }
        if (request.getPaidAt() != null) {
            invoice.setPaidAt(request.getPaidAt());
        }
        if (request.getPaymentMethod() != null) {
            invoice.setPaymentMethod(request.getPaymentMethod());
        }

        return convertToDTO(invoiceRepository.save(invoice));
    }

    /**
     * Marks an invoice as paid and records the payment method.
     *
     * @param id the invoice ID to mark as paid
     * @param paymentMethod the payment method used
     * @return the updated invoice
     * @throws ResourceNotFoundException if invoice not found
     * @throws BadRequestException if invoice is already paid
     */
    @Transactional
    public InvoiceResponseDTO markAsPaid(UUID id, String paymentMethod) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice is already paid");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setPaymentMethod(paymentMethod);

        return convertToDTO(invoiceRepository.save(invoice));
    }

    /**
     * Deletes an invoice.
     *
     * @param id the invoice ID to delete
     * @throws ResourceNotFoundException if invoice not found
     */
    @Transactional
    public void deleteInvoice(UUID id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    /**
     * Converts an Invoice entity to its DTO representation.
     *
     * @param invoice the invoice entity
     * @return the invoice DTO
     */
    private InvoiceResponseDTO convertToDTO(Invoice invoice) {
        return new InvoiceResponseDTO(
                invoice.getId(),
                invoice.getPatient().getId(),
                invoice.getPatient().getFirstName() + " " + invoice.getPatient().getLastName(),
                invoice.getPatient().getPatientCode(),
                invoice.getAppointment() != null ? invoice.getAppointment().getId() : null,
                invoice.getInvoiceNumber(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getAmount(),
                invoice.getTaxAmount(),
                invoice.getStatus(),
                invoice.getDescription(),
                invoice.getPaidAt(),
                invoice.getPaymentMethod()
        );
    }
}