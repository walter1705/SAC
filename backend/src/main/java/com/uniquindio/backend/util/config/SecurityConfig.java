package com.uniquindio.backend.util.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
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
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Swagger/OpenAPI endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/openapi.yaml", "/swagger-ui.html").permitAll()
                // Usuario endpoints - ADMINISTRADOR only
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/estado").hasRole("ADMINISTRADOR")
                // Solicitud endpoints - Role based access
                .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes").hasAnyRole("SOLICITANTE", "GESTOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes").hasAnyRole("SOLICITANTE", "GESTOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/*/historial").hasAnyRole("SOLICITANTE", "GESTOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/*").hasAnyRole("SOLICITANTE", "GESTOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/clasificar").hasRole("GESTOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/estado").hasRole("GESTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes/*/asignar").hasRole("GESTOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/cerrar").hasRole("GESTOR")
                // IA endpoints - Role based access
                .requestMatchers(HttpMethod.POST, "/api/v1/ia/sugerir-clasificacion").hasRole("GESTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/ia/solicitudes/*/resumen").hasAnyRole("SOLICITANTE", "GESTOR", "ADMINISTRADOR")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
