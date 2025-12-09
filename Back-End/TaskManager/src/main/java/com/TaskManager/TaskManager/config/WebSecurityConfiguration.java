package com.TaskManager.TaskManager.config;

import com.TaskManager.TaskManager.enums.UserRole;
import com.TaskManager.TaskManager.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtAuthentificationFilter jwtAuthentificationFilter;
    private final UserService userService;

    // ----------------------
    // CORS Configuration Source
    // ----------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow Angular origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Allow ALL methods including OPTIONS
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

        // Allow ALL headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Expose headers to client
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Cache for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ----------------------
    // Password encoder
    // ----------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ----------------------
    // UserDetailsService bean
    // ----------------------
    @Bean
    public UserDetailsService userDetailsService() {
        return userService.userDetailService();
    }

    // ----------------------
    // Authentication Provider - FIXED with constructor parameter
    // ----------------------
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService()); // Pass UserDetailsService to constructor

        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ----------------------
    // Authentication Manager
    // ----------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ----------------------
    // Security Filter Chain - WITH CORS FIX
    // ----------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CRITICAL: Enable CORS with configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF for stateless API
                .csrf(csrf -> csrf.disable())

                // Configure authorization
                .authorizeHttpRequests(request -> request
                        // CRITICAL: Allow OPTIONS requests for CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints (no authentication needed)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/check-auth").authenticated()
                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasAuthority(UserRole.ADMIN.name())

                        // Employee endpoints
                        .requestMatchers("/api/employee/**").hasAuthority(UserRole.EMPLOYEE.name())

                        // All other requests need authentication
                        .anyRequest().authenticated()
                )

                // Use stateless session (no session cookies)
                .sessionManagement(manager -> manager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add authentication provider
                .authenticationProvider(authenticationProvider())

                // Add JWT filter BEFORE username/password authentication filter
                .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}