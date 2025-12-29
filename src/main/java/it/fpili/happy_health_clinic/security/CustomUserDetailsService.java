package it.fpili.happy_health_clinic.security;

import it.fpili.happy_health_clinic.entities.User;
import it.fpili.happy_health_clinic.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of UserDetailsService for Spring Security.
 * Loads user authentication details from the database based on email address.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing User entities from the database.
     */
    private final UserRepository userRepository;

    /**
     * Loads user authentication details by email address.
     * This method is called by Spring Security during the authentication process.
     *
     * @param email the user's email address (username), must not be null
     * @return non-null UserDetails containing the user's authentication information
     * @throws UsernameNotFoundException if no user is found with the provided email
     * @throws NullPointerException if email parameter is null
     */
    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }
}