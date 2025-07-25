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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityDeltaConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final CorsConfig corsConfig;

    public SecurityDeltaConfig(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter, CorsConfig corsConfig) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.corsConfig = corsConfig;
    }

    private void commonSecurityConfig(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
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
    @Order(0)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        commonSecurityConfig(http);

        http.securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        // Público
                        .requestMatchers(
                                "/auth/login/**",
                                "/auth/register/**",
                                "/auth/refresh/**",
                                "/auth/revoke/**",
                                "/auth/forgot-password/**",
                                "/auth/verify-code/**",
                                "/auth/reset-password/**",
                                "/account/register/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers(
                                "/admin/**",
                                "/settings/**"
                        ).hasRole(Roles.ADMIN.name())

                        // MONITOR
                        .requestMatchers(
                                "/activities/monitor/**",
                                "/tutoring/register"
                        ).hasAnyRole(Roles.MONITOR.name(), Roles.ADMIN.name())

                        .requestMatchers(
                                "/tutoring/subjects"
                        ).hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())

                        // STUDENT / MONITOR / ADMIN
                        .requestMatchers(
                                "/activities/list",
                                "/activities/list-miniatures",
                                "/activities/search",
                                "/activities/get-file-link/**",
                                "/activities/get-file/**",
                                "/activities/get/**",
                                "/account/register/tutor",
                                "/account/profile/create",
                                "/account/profile/delete",
                                "/account/profile/update",
                                "/account/profile/upload-photo-profile",
                                "/account/profile/download-photo-profile",
                                "/account/profile/photo-profile-url",
                                "/account/profile/delete-photo-profile",
                                "/auth/get-user-info",
                                "/auth/change-password"
                        ).hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())

                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String json = """
            {
              "error": "Não autenticado",
              "message": "Credenciais incorretas, token inválido, expirado ou ausente."
            }
            """;
            response.getWriter().write(json);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            String json = """
            {
              "error": "Acesso negado",
              "message": "Você não tem permissão para acessar este recurso."
            }
            """;
            response.getWriter().write(json);
        };
    }
}
