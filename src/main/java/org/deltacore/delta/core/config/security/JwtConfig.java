package org.deltacore.delta.core.config.security;

import jakarta.annotation.PostConstruct;
import org.deltacore.delta.core.config.security.filter.JwtAuthFilter;
import org.deltacore.delta.domains.auth.service.DeltaUserDetailsService;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HMACSHA256");
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(this.secretKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("role");
        converter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthConverter;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtDecoder jwtDecoder, TokenBlacklistService blacklistService, DeltaUserDetailsService userDetailsService) {
        return new JwtAuthFilter(jwtDecoder, blacklistService, userDetailsService);
    }

}
