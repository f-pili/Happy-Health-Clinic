package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for creating a new invoice.
 * Contains the required information to generate an invoice for a patient appointment.
 */
@Data
public class CreateInvoiceRequestDTO {

    /**
     * The unique identifier of the patient being invoiced.
     */
    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    /**
     * The unique identifier of the appointment associated with this invoice.
     */
    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;

    /**
     * The date the invoice was issued.
     * Cannot be in the future.
     */
    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    private LocalDate issueDate;

    /**
     * The date the payment is due.
     * Must be in the future.
     */
    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    /**
     * The invoice amount before tax.
     * Must be greater than zero.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * The tax amount applied to the invoice.
     * Must be zero or greater.
     */
    @NotNull(message = "Tax amount is required")
    @DecimalMin(value = "0.0", message = "Tax amount must be 0 or greater")
    private BigDecimal taxAmount;

    /**
     * A description of the charges included in the invoice.
     */
    @NotBlank(message = "Description is required")
    private String description;
}
