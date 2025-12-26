package it.fpili.happy_health_clinic.dto.response;

import it.fpili.happy_health_clinic.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for authentication response data.
 * Contains user information and authentication token returned after successful login or registration.
 */
@Data
@AllArgsConstructor
public class AuthResponseDTO {

    /**
     * The unique identifier of the authenticated user.
     */
    private UUID id;

    /**
     * The user's first name.
     */
    private String firstName;

    /**
     * The user's last name.
     */
    private String lastName;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's role in the system.
     */
    private Role role;

    /**
     * The JWT access token for authenticating subsequent API requests.
     */
    private String accessToken;
}
