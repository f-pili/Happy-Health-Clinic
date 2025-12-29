package it.fpili.happy_health_clinic.security;

import it.fpili.happy_health_clinic.entities.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of UserDetails for Spring Security.
 * Wraps the User entity to provide authentication and authorization details.
 * This class is immutable and guarantees that the user reference is never null.
 */
@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    /**
     * The User entity associated with these security details.
     * This field is guaranteed to be non-null through constructor validation.
     */
    @lombok.NonNull
    private final User user;

    /**
     * Returns the authorities granted to the user based on their role.
     * The role is prefixed with "ROLE_" as required by Spring Security.
     *
     * @return a non-null collection containing a single GrantedAuthority representing the user's role
     */
    @Override
    @lombok.NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Returns the user's password for authentication.
     *
     * @return the non-null hashed password
     */
    @Override
    @lombok.NonNull
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the user's username (email address) for authentication.
     * In this implementation, the email serves as the unique username.
     *
     * @return the non-null user's email address
     */
    @Override
    @lombok.NonNull
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     * In this implementation, accounts never expire.
     *
     * @return always true, indicating the account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * In this implementation, accounts are never locked.
     *
     * @return always true, indicating the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * In this implementation, credentials never expire.
     *
     * @return always true, indicating the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is enabled and active.
     * In this implementation, all accounts are considered enabled.
     *
     * @return always true, indicating the account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}