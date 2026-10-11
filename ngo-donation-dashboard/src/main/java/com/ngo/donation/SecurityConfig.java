
package com.ngo.donation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        System.out.println("MY SECURITY CONFIG IS LOADED");

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                .requestMatchers(
                    "/api/otp/send",
                    "/api/otp/verify",
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // Campaigns
                .requestMatchers("/api/campaigns/**").permitAll()

                // Help requests
                .requestMatchers(HttpMethod.POST, "/api/help-requests")
                    .hasAnyRole("HELP_SEEKER", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/help-requests/my")
                    .hasAnyRole("HELP_SEEKER", "ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/help-requests/*/verify"
                ).hasRole("VOLUNTEER")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/help-requests/*/approve",
                    "/api/help-requests/*/reject"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/help-requests",
                    "/api/help-requests/status/**"
                ).hasAnyRole("ADMIN", "VOLUNTEER")

                // Donations
                .requestMatchers("/api/donations/**")
                    .hasAnyRole("ADMIN", "DONOR")

                // Beneficiaries
                .requestMatchers("/api/beneficiaries/**")
                    .hasAnyRole("ADMIN", "VOLUNTEER")

                // Fund utilization
                .requestMatchers("/api/fund-utilization/**")
                    .hasRole("ADMIN")

                // Impact reports
                .requestMatchers(HttpMethod.GET, "/api/impact-reports/**")
                    .hasAnyRole(
                        "ADMIN",
                        "DONOR",
                        "VOLUNTEER",
                        "HELP_SEEKER"
                    )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/impact-reports/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/impact-reports/**"
                ).hasRole("ADMIN")

                // Volunteer approval/rejection: Admin only
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/volunteers/*/approve",
                    "/api/volunteers/*/reject"
                ).hasRole("ADMIN")

                // Volunteer list and read endpoints
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/volunteers/**"
                ).hasAnyRole("ADMIN", "VOLUNTEER")

                // Creating or deleting volunteer records: Admin only
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/volunteers/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/volunteers/**"
                ).hasRole("ADMIN")

                // Other volunteer updates are Admin only
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/volunteers/**"
                ).hasRole("ADMIN")

                // User management
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
