package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for invoice response data.
 * Contains complete invoice information including patient details and payment status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {

    /**
     * The unique identifier of the invoice.
     */
    private UUID id;

    /**
     * The unique identifier of the patient being invoiced.
     */
    private UUID patientId;

    /**
     * The full name of the patient.
     */
    private String patientName;

    /**
     * The patient code for quick identification.
     */
    private String patientCode;

    /**
     * The unique identifier of the associated appointment.
     */
    private UUID appointmentId;

    /**
     * The unique invoice number.
     */
    private String invoiceNumber;

    /**
     * The date the invoice was issued.
     */
    private LocalDate issueDate;

    /**
     * The date payment is due.
     */
    private LocalDate dueDate;

    /**
     * The invoice amount before tax.
     */
    private BigDecimal amount;

    /**
     * The tax amount applied to the invoice.
     */
    private BigDecimal taxAmount;

    /**
     * The current status of the invoice.
     */
    private InvoiceStatus status;

    /**
     * A description of the charges included in the invoice.
     */
    private String description;

    /**
     * The date and time the invoice was paid.
     */
    private LocalDateTime paidAt;

    /**
     * The payment method used to pay the invoice.
     */
    private String paymentMethod;
}
