package com.example.academic_system.config;

import com.example.academic_system.services.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final ActivityLogService activityLogService;

    // Constructor injection untuk menghindari circular dependency
    public SecurityConfig(@Lazy CustomSuccessHandler customSuccessHandler,
                          ActivityLogService activityLogService) {
        this.customSuccessHandler = customSuccessHandler;
        this.activityLogService = activityLogService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/public/**", "/images/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/dosen/**").hasRole("DOSEN")
                        .requestMatchers("/mahasiswa/**").hasRole("MAHASISWA")

                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/dosen/**").hasRole("DOSEN")
                        .requestMatchers("/api/mahasiswa/**").hasRole("MAHASISWA")
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                String username = authentication.getName();
                                String detail = String.format("User %s logged out from IP: %s",
                                        username, getClientIP(request));
                                try {
                                    activityLogService.log("Authentication", username, "LOGOUT", detail, username);
                                } catch (Exception e) {
                                    // Log error tapi jangan block logout
                                    System.err.println("Failed to log logout activity: " + e.getMessage());
                                }
                            }
                            response.sendRedirect("/login?logout");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.sendRedirect("/access-denied");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login");
                        })
                )

                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    private String getClientIP(jakarta.servlet.http.HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.trim().isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}