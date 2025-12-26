package it.fpili.happy_health_clinic.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * Typically corresponds to an HTTP 404 Not Found response.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a ResourceNotFoundException with a detailed error message.
     *
     * @param message the error message describing which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
