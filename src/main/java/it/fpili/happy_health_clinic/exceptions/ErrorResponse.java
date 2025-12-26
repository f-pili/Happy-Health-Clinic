package it.fpili.happy_health_clinic.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for standardized error responses.
 * Used to provide consistent error information to API clients.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The HTTP status code of the error response.
     */
    private int status;

    /**
     * A short error type or category description.
     */
    private String error;

    /**
     * A detailed error message providing information about what went wrong.
     */
    private String message;
}
