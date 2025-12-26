package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for updating an existing invoice.
 * All fields are optional to allow partial updates.
 */
@Data
public class UpdateInvoiceRequestDTO {

    /**
     * The updated due date for payment.
     */
    private LocalDate dueDate;

    /**
     * The updated invoice amount before tax.
     * Must be greater than zero if provided.
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * The updated tax amount applied to the invoice.
     * Must be zero or greater if provided.
     */
    @DecimalMin(value = "0.0", message = "Tax amount must be 0 or greater")
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
