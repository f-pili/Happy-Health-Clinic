package it.fpili.happy_health_clinic.security;

import it.fpili.happy_health_clinic.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of UserDetails for Spring Security.
 * Wraps the User entity to provide authentication and authorization details.
 */
@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    /**
     * The User entity associated with these security details.
     */
    private User user;

    /**
     * Returns the authorities granted to the user based on their role.
     *
     * @return a collection containing a single GrantedAuthority representing the user's role
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Returns the user's password for authentication.
     *
     * @return the hashed password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the user's username (email address) for authentication.
     *
     * @return the user's email address
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true since accounts do not expire
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true since accounts are not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true since credentials do not expire
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is enabled.
     *
     * @return true since all accounts are enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
