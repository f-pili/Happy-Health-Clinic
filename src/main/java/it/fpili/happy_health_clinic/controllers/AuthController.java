package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.dto.request.LoginRequestDTO;
import it.fpili.happy_health_clinic.dto.request.RegisterRequestDTO;
import it.fpili.happy_health_clinic.dto.response.AuthResponseDTO;
import it.fpili.happy_health_clinic.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Handles user registration and login requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user.
     *
     * @param request the user registration data
     * @return authentication response containing user details and JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the user login credentials
     * @return authentication response containing user details and JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}