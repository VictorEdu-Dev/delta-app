package org.deltacore.delta.core.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final TokenBlacklistService blacklistService;
    private final JwtTokenService tokenProvider;

    @Autowired
    public JwtAuthFilter(JwtDecoder jwtDecoder, TokenBlacklistService blacklistService, JwtTokenService tokenProvider) {
        this.jwtDecoder = jwtDecoder;
        this.blacklistService = blacklistService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/home/login") || path.equals("/home/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtValidationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token: " + ex.getMessage());
            return;
        } catch (BadJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Bad Token: " + ex.getMessage());
            return;
        }


        String username = jwt.getSubject();
        String role = jwt.getClaimAsString("role");
        Instant expiresAt = jwt.getExpiresAt();
        Instant now = Instant.now();
        long minutesLeft = Duration.between(now, expiresAt).toMinutes();
        int TIME_TO_REFRESH = 10;

        if (minutesLeft < TIME_TO_REFRESH) {
            if (blacklistService.isBlacklisted(token, username)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String newToken = tokenProvider.generateToken(username, role);
            blacklistService.add(token, expiresAt, username);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
        }
        filterChain.doFilter(request, response);
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            return authHeader.replace("Bearer", "").trim();
        }
        return null;
    }
}
