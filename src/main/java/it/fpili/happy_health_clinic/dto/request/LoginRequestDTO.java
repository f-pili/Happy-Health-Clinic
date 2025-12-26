package it.fpili.happy_health_clinic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for user login requests.
 * Contains credentials required to authenticate a user.
 */
@Data
public class LoginRequestDTO {

    /**
     * The user's email address.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The user's password.
     */
    @NotBlank(message = "Password is required")
    private String password;
}