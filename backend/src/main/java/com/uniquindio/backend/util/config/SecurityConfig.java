package com.uniquindio.backend.util.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uniquindio.backend.util.security.JwtAuthenticationEntryPoint;
import com.uniquindio.backend.util.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/signup").permitAll()
                // Swagger/OpenAPI endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/openapi.yaml", "/swagger-ui.html").permitAll()
                // Usuario endpoints - ADMINISTRADOR only
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/estado").hasRole("ADMINISTRADOR")
                // Solicitud endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes").access(adminOrAnyOf("SOLICITANTE", "GESTOR"))
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes").access(adminOrAnyOf("SOLICITANTE", "GESTOR"))
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/*/historial").access(adminOrAnyOf("SOLICITANTE", "GESTOR"))
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/*").access(adminOrAnyOf("SOLICITANTE", "GESTOR"))
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/clasificar").access(adminOrAnyOf("GESTOR"))
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/estado").access(adminOrAnyOf("GESTOR"))
                .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes/*/asignar").access(adminOrAnyOf("GESTOR"))
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/cerrar").access(adminOrAnyOf("GESTOR"))
                // IA endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/ia/sugerir-clasificacion").access(adminOrAnyOf("GESTOR"))
                .requestMatchers(HttpMethod.GET, "/api/v1/ia/solicitudes/*/resumen").access(adminOrAnyOf("SOLICITANTE", "GESTOR"))
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    /**
     * ADMINISTRADOR bypasses all role checks. Other roles must match at least one of the provided roles.
     */
    private static AuthorizationManager<RequestAuthorizationContext> adminOrAnyOf(String... roles) {
        return (authentication, context) -> {
            var auth = authentication.get();
            if (!auth.isAuthenticated()) return new AuthorizationDecision(false);
            boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
            if (isAdmin) return new AuthorizationDecision(true);
            for (String role : roles) {
                boolean hasRole = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
                if (hasRole) return new AuthorizationDecision(true);
            }
            return new AuthorizationDecision(false);
        };
    }
}
