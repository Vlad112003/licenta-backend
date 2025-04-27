package com.elearning.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    ///
    /// Security Filter Chain
    /// params: HttpSecurity
    /// return: SecurityFilterChain
    /// description: This method configures the security filter chain for the application.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .anyRequest().permitAll() // Allows public access to all routes
                )
                .csrf(AbstractHttpConfigurer::disable); // Disables CSRF for RESTful applications

        return http.build();
    }

    ///
    /// Password Encoder
    /// params: None
    /// return: PasswordEncoder
    /// description: This method creates a PasswordEncoder bean using BCryptPasswordEncoder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}