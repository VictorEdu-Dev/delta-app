package org.deltacore.delta.core.config.security;

import org.deltacore.delta.core.config.security.filter.JwtAuthFilter;
import org.deltacore.delta.domains.auth.model.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityDeltaConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityDeltaConfig(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter, JwtAuthFilter jwtAuthFilter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    private void commonSecurityConfig(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter))
                );
    }

    @Bean
    @Order(1)
    public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/auth/login/**", "/auth/register/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        commonSecurityConfig(http);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/**", "/settings/**", "/home/get/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/manage/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers("/settings/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers("/home/get/**").hasRole(Roles.ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        commonSecurityConfig(http);
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain monitorSecurityChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/activities/monitor/**", "tutoring/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/activities/monitor/**").hasAnyRole(Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/tutoring/**").hasAnyRole(Roles.MONITOR.name(), Roles.ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        commonSecurityConfig(http);
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain studentSecurityChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/activities/list", "/activities/get/**", "/activities/list-activities-tsdt", "/activities/search")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/activities/list").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/get/**").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/list-activities-tsdt").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/search").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        commonSecurityConfig(http);
        return http.build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain defaultSecurityChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
