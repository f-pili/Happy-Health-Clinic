package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract entity representing a user in the system.
 * Serves as a base class for specific user types (Patient, Employee, etc.).
 * Uses JOINED inheritance strategy for database table organization.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class User {

    /**
     * The unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The user's first name.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * The user's last name.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * The user's email address.
     * Must be unique across all users.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The user's hashed password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The user's phone number.
     */
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    /**
     * The user's date of birth.
     */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * URL to the user's avatar image.
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * The user's role in the system.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    /**
     * The user's emergency contact information.
     */
    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    /**
     * The timestamp when the user account was created.
     * Automatically set on entity creation and cannot be updated.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
