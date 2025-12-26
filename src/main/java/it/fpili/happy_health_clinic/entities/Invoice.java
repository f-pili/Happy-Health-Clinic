package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an invoice.
 * Stores billing information for patient appointments.
 */
@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    /**
     * The unique identifier of the invoice.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The patient being invoiced.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * The appointment associated with this invoice.
     * One-to-one relationship.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    /**
     * The unique invoice number.
     */
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    /**
     * The date the invoice was issued.
     * Defaults to the current date.
     */
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate = LocalDate.now();

    /**
     * The date payment is due.
     */
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * The invoice amount before tax.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The tax amount applied to the invoice.
     */
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    /**
     * The current status of the invoice.
     * Defaults to PENDING.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private InvoiceStatus status = InvoiceStatus.PENDING;

    /**
     * A description of the charges included in the invoice.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The date and time the invoice was paid.
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /**
     * The payment method used to pay the invoice.
     */
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
}
