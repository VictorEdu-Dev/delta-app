package org.deltacore.delta.core.config.security;

import org.deltacore.delta.domains.profile.model.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityDeltaConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityDeltaConfig(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    private void commonSecurityConfig(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
        commonSecurityConfig(http);
        return http
                .securityMatcher("/auth/login/**",
                        "/auth/register/**",
                        "/auth/refresh/**",
                        "/auth/revoke/**",
                        "/account/register/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain adminSecurityChain(HttpSecurity http) throws Exception {
        commonSecurityConfig(http);
        http.securityMatcher("/admin/**", "/settings/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole(Roles.ADMIN.name()))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain monitorSecurityChain(HttpSecurity http) throws Exception {
        commonSecurityConfig(http);
        http.securityMatcher("/activities/monitor/**", "/tutoring/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/tutoring/subjects", "/tutoring/register").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .anyRequest().hasAnyRole(Roles.MONITOR.name(), Roles.ADMIN.name()))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
        return http.build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain studentSecurityChain(HttpSecurity http) throws Exception {
        commonSecurityConfig(http);
        http.securityMatcher(
                        "/activities/list",
                        "/activities/list-miniatures",
                        "/activities/search",
                        "/account/register/tutor",
                        "/account/profile/create",
                        "/account/profile/delete",
                        "/account/profile/update",
                        "/auth/get-user-info")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasAnyRole(
                                Roles.STUDENT.name(),
                                Roles.MONITOR.name(),
                                Roles.ADMIN.name()))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
        return http.build();
    }

    @Bean
    @Order(6)
    public SecurityFilterChain defaultSecurityChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth
                                .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://delta-front-sage.vercel.app",
                "https://delta-app-410394653851.southamerica-east1.run.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin", "Cache-Control", "X-Requested-With"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
