package it.fpili.happy_health_clinic.dto.request;

import it.fpili.happy_health_clinic.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for user registration requests.
 * Contains personal information required to create a new user account in the system.
 */
@Data
public class RegisterRequestDTO {

    /**
     * The user's first name.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * The user's last name.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * The user's email address.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * The user's password.
     * Must contain at least 8 characters including uppercase, lowercase, digit, and special character.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    /**
     * The user's phone number.
     */
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    /**
     * The user's date of birth.
     * Must be in the past.
     */
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * The user's role in the system.
     */
    @NotNull(message = "Role is required")
    private Role role;

    /**
     * The user's tax identification number.
     * Must be exactly 16 characters (Italian Codice Fiscale format).
     */
    @NotBlank(message = "Tax ID is required")
    @Size(min = 16, max = 16, message = "Tax ID must be exactly 16 characters")
    private String taxId;

    /**
     * The user's street address.
     */
    @NotBlank(message = "Address is required")
    private String address;

    /**
     * The user's city or municipality.
     */
    @NotBlank(message = "City is required")
    private String city;

    /**
     * The user's province or region.
     */
    @NotBlank(message = "Province is required")
    private String province;

    /**
     * The user's postal code.
     */
    @NotBlank(message = "Zip code is required")
    private String zipCode;
}