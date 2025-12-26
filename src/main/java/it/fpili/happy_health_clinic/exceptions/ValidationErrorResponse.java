package it.fpili.happy_health_clinic.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for validation error responses.
 * Provides detailed field-level validation error information to API clients.
 */
@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    /**
     * The timestamp when the validation error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The HTTP status code of the error response.
     */
    private int status;

    /**
     * A short error type description indicating validation failure.
     */
    private String error;

    /**
     * A map of field names to their corresponding validation error messages.
     */
    private Map<String, String> errors;
}
