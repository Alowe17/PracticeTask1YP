package com.audit.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@Slf4j
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/audit/api/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            log.warn("Запрос без аутентификации: {} {}. Причина: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
                            writeSecurityError(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication is required");
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            log.warn("Доступ запрещён: {} {}. Причина: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
                            writeSecurityError(response, HttpServletResponse.SC_FORBIDDEN, "Access is denied");
                        })
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {})
                );

        return http.build();
    }

    private void writeSecurityError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":" + status + ",\"error\":\"" + message + "\"}");
    }
}
