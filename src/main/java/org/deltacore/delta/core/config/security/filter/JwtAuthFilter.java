package org.deltacore.delta.core.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.deltacore.delta.domains.auth.model.DeltaUserDetails;
import org.deltacore.delta.domains.auth.service.DeltaUserDetailsService;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
        private final DeltaUserDetailsService userDetailsService;

        @Autowired
        public JwtAuthFilter(JwtDecoder jwtDecoder, TokenBlacklistService blacklistService, DeltaUserDetailsService userDetailsService) {
            this.jwtDecoder = jwtDecoder;
            this.blacklistService = blacklistService;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain) throws ServletException, IOException {

            String path = request.getRequestURI();
            if (
                    path.equals("/auth/login") ||
                    path.equals("/auth/register") ||
                    path.equals("/auth/refresh")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Jwt jwt;
            try {
                jwt = jwtDecoder.decode(token);
            } catch (JwtValidationException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid Token: " + ex.getMessage() + "\"}");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                return;
            } catch (BadJwtException ex) {
                // Token malformado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Bad Token: " + ex.getMessage() + "\"}");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                return;
            }

            if (blacklistService.isBlacklisted(jwt.getId(), jwt.getSubject())) { //
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //
                response.getWriter().write("{\"error\": \"Token has been blacklisted.\"}"); //
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                return;
            }

            DeltaUserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
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
