package it.fpili.happy_health_clinic.enums;

/**
 * Enumeration of possible invoice statuses.
 */
public enum InvoiceStatus {
    /**
     * Invoice has been issued and payment is awaited.
     */
    PENDING,

    /**
     * Invoice has been paid in full.
     */
    PAID,

    /**
     * Invoice payment is past the due date.
     */
    OVERDUE,

    /**
     * Invoice has been cancelled.
     */
    CANCELLED
}
