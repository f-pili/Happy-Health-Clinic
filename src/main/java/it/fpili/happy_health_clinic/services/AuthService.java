package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.dto.request.LoginRequestDTO;
import it.fpili.happy_health_clinic.dto.request.RegisterRequestDTO;
import it.fpili.happy_health_clinic.dto.response.AuthResponseDTO;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.entities.User;
import it.fpili.happy_health_clinic.enums.Role;
import it.fpili.happy_health_clinic.exceptions.BadRequestException;
import it.fpili.happy_health_clinic.exceptions.ResourceNotFoundException;
import it.fpili.happy_health_clinic.repositories.PatientRepository;
import it.fpili.happy_health_clinic.repositories.UserRepository;
import it.fpili.happy_health_clinic.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling user authentication and registration.
 * Manages user registration, login, and JWT token generation.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Repository for user data access.
     */
    private final UserRepository userRepository;

    /**
     * Repository for patient data access.
     */
    private final PatientRepository patientRepository;

    /**
     * Password encoder for secure password hashing.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT utility for token generation and validation.
     */
    private final JwtUtil jwtUtil;

    /**
     * Spring Security authentication manager.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Service for generating unique codes.
     */
    private final CodeGeneratorService codeGeneratorService;

    /**
     * Service for generating avatar URLs.
     */
    private final AvatarService avatarService;

    /**
     * Service for sending emails.
     */
    private final EmailService emailService;

    /**
     * Registers a new user account.
     * Currently supports patient role registration.
     * Generates unique patient code, avatar, and sends welcome email.
     *
     * @param request the user registration data
     * @return authentication response containing user info and JWT token
     * @throws BadRequestException if email or tax ID already exists or if role is not PATIENT
     */
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (request.getRole() == Role.PATIENT) {
            if (patientRepository.existsByTaxId(request.getTaxId())) {
                throw new BadRequestException("Tax ID already exists");
            }

            Patient patient = new Patient();

            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setEmail(request.getEmail());
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
            patient.setPhoneNumber(request.getPhoneNumber());
            patient.setDateOfBirth(request.getDateOfBirth());
            patient.setRole(Role.PATIENT);

            patient.setAvatarUrl(avatarService.generateAvatarUrl(
                    request.getFirstName(),
                    request.getLastName()
            ));

            patient.setPatientCode(codeGeneratorService.generatePatientCode());
            patient.setTaxId(request.getTaxId());
            patient.setAddress(request.getAddress());
            patient.setCity(request.getCity());
            patient.setProvince(request.getProvince());
            patient.setZipCode(request.getZipCode());

            Patient savedPatient = patientRepository.save(patient);

            try {
                emailService.sendWelcomeEmail(savedPatient);
            } catch (Exception e) {
            }

            String token = jwtUtil.generateToken(savedPatient.getEmail());

            return new AuthResponseDTO(
                    savedPatient.getId(),
                    savedPatient.getFirstName(),
                    savedPatient.getLastName(),
                    savedPatient.getEmail(),
                    savedPatient.getRole(),
                    token
            );
        }

        throw new BadRequestException("Only PATIENT registration is supported via this endpoint");
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login credentials
     * @return authentication response containing user info and JWT token
     * @throws ResourceNotFoundException if user is not found after successful authentication
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}