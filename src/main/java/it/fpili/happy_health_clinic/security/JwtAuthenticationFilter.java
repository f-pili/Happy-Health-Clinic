package it.fpili.happy_health_clinic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter for Spring Security.
 * Extracts and validates JWT tokens from request headers and sets up authentication context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Utility for JWT token validation and claim extraction.
     */
    private final JwtUtil jwtUtil;

    /**
     * Service for loading user authentication details from the database.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Processes each HTTP request to extract and validate JWT tokens.
     * If a valid token is found, sets up the authentication context.
     *
     * @param request the HTTP request, must not be null
     * @param response the HTTP response, must not be null
     * @param filterChain the filter chain for continuing the request, must not be null
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if any parameter is null
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether this filter should be skipped for certain requests.
     * Skips authentication filtering for endpoints under /auth/** to allow public access.
     *
     * @param request the HTTP request, must not be null
     * @return true if the filter should be skipped, false otherwise
     * @throws NullPointerException if request is null
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match("/auth/**", request.getServletPath());
    }
}