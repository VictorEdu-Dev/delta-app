package org.deltacore.delta.config;

import jakarta.servlet.http.HttpServletResponse;
import org.deltacore.delta.config.security.JwtAuthFilter;
import org.deltacore.delta.model.user.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityDeltaConfig {
    private final JwtAuthFilter jwtFilter;

    @Autowired
    public SecurityDeltaConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain loginSecurityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http
                .securityMatcher("/home/login", "/home/register", "/auth/**") // <- limita esse filtro
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home/login", "/home/register").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authManager)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                )
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/admin/**", "/settings/**", "/v3/api-docs/**", "/swagger-ui/**", "/home/get/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/manage/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers("/settings/**").hasAnyRole(Roles.ADMIN.name())
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").hasAnyRole(Roles.ADMIN.name())
                        .requestMatchers("/home/get/**").hasAnyRole(Roles.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain monitorSecurityChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/activities/monitor/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/activities/monitor/**").hasAnyRole(Roles.MONITOR.name(), Roles.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain studentSecurityChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/activities/list", "/activities/get/**", "/activities/list-activities-tsdt", "/activities/search")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/activities/list").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/get/**").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/list-activities-tsdt").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .requestMatchers("/activities/search").hasAnyRole(Roles.STUDENT.name(), Roles.MONITOR.name(), Roles.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain defaultSecurityChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
