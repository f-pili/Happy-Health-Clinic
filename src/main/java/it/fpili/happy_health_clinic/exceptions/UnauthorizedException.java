package it.fpili.happy_health_clinic.exceptions;

/**
 * Exception thrown when a user is not authenticated or lacks valid credentials.
 * Typically corresponds to an HTTP 401 Unauthorized response.
 */
public class UnauthorizedException extends RuntimeException {
    /**
     * Constructs an UnauthorizedException with a detailed error message.
     *
     * @param message the error message describing the authentication failure
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
