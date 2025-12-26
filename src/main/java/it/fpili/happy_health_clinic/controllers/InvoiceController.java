package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.CreateInvoiceRequestDTO;
import it.fpili.happy_health_clinic.dto.request.UpdateInvoiceRequestDTO;
import it.fpili.happy_health_clinic.dto.response.InvoiceResponseDTO;
import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import it.fpili.happy_health_clinic.services.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing invoices.
 * Provides endpoints for retrieving, creating, updating, and deleting invoice records.
 */
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /**
     * Retrieves all invoices with pagination support.
     *
     * @param pageable pagination and sorting parameters
     * @return paginated list of invoices
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<InvoiceResponseDTO>> getAllInvoices(Pageable pageable) {
        Page<InvoiceResponseDTO> invoices = invoiceService.getAllInvoices(pageable);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the unique identifier of the invoice
     * @return the invoice matching the provided ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable UUID id) {
        InvoiceResponseDTO invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    /**
     * Retrieves an invoice by its invoice number.
     *
     * @param invoiceNumber the invoice number to search for
     * @return the invoice matching the provided number
     */
    @GetMapping("/number/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        InvoiceResponseDTO invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(invoice);
    }

    /**
     * Retrieves all invoices for a specific patient.
     *
     * @param patientId the unique identifier of the patient
     * @return list of invoices for the patient
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoicesByPatient(@PathVariable UUID patientId) {
        List<InvoiceResponseDTO> invoices = invoiceService.getInvoicesByPatient(patientId);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Retrieves invoices filtered by status.
     *
     * @param status the invoice status to filter by
     * @return list of invoices with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        List<InvoiceResponseDTO> invoices = invoiceService.getInvoicesByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Retrieves all overdue invoices based on the provided current date.
     *
     * @param currentDate the reference date to determine overdue status (ISO 8601 format)
     * @return list of overdue invoices
     */
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InvoiceResponseDTO>> getOverdueInvoices(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        List<InvoiceResponseDTO> invoices = invoiceService.getOverdueInvoices(currentDate);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Calculates the total amount of invoices grouped by status.
     *
     * @param status the invoice status to calculate total for
     * @return the total amount for invoices with the specified status
     */
    @GetMapping("/total/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> calculateTotalByStatus(@PathVariable InvoiceStatus status) {
        BigDecimal total = invoiceService.calculateTotalByStatus(status);
        return ResponseEntity.ok(total);
    }

    /**
     * Creates a new invoice.
     *
     * @param request the invoice creation data
     * @return the newly created invoice
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO request) {
        InvoiceResponseDTO invoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    /**
     * Updates an existing invoice.
     *
     * @param id the unique identifier of the invoice to update
     * @param request the updated invoice data
     * @return the updated invoice
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInvoiceRequestDTO request) {
        InvoiceResponseDTO updatedInvoice = invoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(updatedInvoice);
    }

    /**
     * Marks an invoice as paid.
     *
     * @param id the unique identifier of the invoice to mark as paid
     * @param paymentMethod the payment method used for the transaction
     * @return the updated invoice with paid status
     */
    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<InvoiceResponseDTO> markAsPaid(
            @PathVariable UUID id,
            @RequestParam String paymentMethod) {
        InvoiceResponseDTO paidInvoice = invoiceService.markAsPaid(id, paymentMethod);
        return ResponseEntity.ok(paidInvoice);
    }

    /**
     * Deletes an invoice.
     *
     * @param id the unique identifier of the invoice to delete
     * @return empty response with 204 status code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}