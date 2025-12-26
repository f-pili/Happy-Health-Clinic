package it.fpili.happy_health_clinic.exceptions;

/**
 * Exception thrown when a request is invalid or malformed.
 * Typically corresponds to an HTTP 400 Bad Request response.
 */
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a BadRequestException with a detailed error message.
     *
     * @param message the error message describing what was invalid about the request
     */
    public BadRequestException(String message) {
        super(message);
    }
}
