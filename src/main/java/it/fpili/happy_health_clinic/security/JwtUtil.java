package it.fpili.happy_health_clinic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility component for JWT token operations.
 * Handles generation, validation, and claim extraction from JWT tokens.
 */
@Component
public class JwtUtil {

    /**
     * The secret key used to sign and verify JWT tokens.
     * Configured via the jwt.secret property.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * The expiration time in milliseconds for generated JWT tokens.
     * Configured via the jwt.expiration property.
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generates a new JWT token for the specified username.
     *
     * @param username the username (email) to include in the token
     * @return a signed JWT token string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * Validates a JWT token by verifying its signature and checking if it has not expired.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username (subject claim) from a JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token
     * @return a Claims object containing all token claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
