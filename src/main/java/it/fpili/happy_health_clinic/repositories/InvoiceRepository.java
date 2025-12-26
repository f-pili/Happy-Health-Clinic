package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Invoice;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing and managing Invoice entities.
 * Provides CRUD operations and custom queries for invoice data.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    /**
     * Finds an invoice by its invoice number.
     *
     * @param invoiceNumber the invoice number to search for
     * @return an Optional containing the invoice if found, empty otherwise
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Finds all invoices for a specific patient.
     *
     * @param patient the patient to search for
     * @return list of invoices associated with the patient
     */
    List<Invoice> findByPatient(Patient patient);

    /**
     * Finds all invoices with a specific status.
     *
     * @param status the invoice status to filter by
     * @return list of invoices with the specified status
     */
    List<Invoice> findByStatus(InvoiceStatus status);

    /**
     * Finds all invoices for a patient with a specific status.
     *
     * @param patient the patient to search for
     * @param status the invoice status to filter by
     * @return list of invoices matching both criteria
     */
    List<Invoice> findByPatientAndStatus(Patient patient, InvoiceStatus status);

    /**
     * Finds all invoices issued within a specified date range.
     *
     * @param start the start date of the range
     * @param end the end date of the range
     * @return list of invoices issued within the specified range
     */
    @Query("SELECT i FROM Invoice i WHERE i.issueDate BETWEEN :start AND :end")
    List<Invoice> findByIssueDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    /**
     * Calculates the total amount of invoices grouped by status.
     *
     * @param status the invoice status to calculate total for
     * @return the sum of all invoice amounts with the specified status
     */
    @Query("SELECT SUM(i.amount) FROM Invoice i WHERE i.status = :status")
    BigDecimal calculateTotalByStatus(@Param("status") InvoiceStatus status);

    /**
     * Finds all overdue invoices (pending invoices with past due dates).
     *
     * @param date the reference date for determining overdue status
     * @return list of overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.status = 'PENDING' AND i.dueDate < :date")
    List<Invoice> findOverdueInvoices(@Param("date") LocalDate date);
}
