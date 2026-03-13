package com.uniquindio.backend.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.backend.model.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(401)
                .error("No Autorizado")
                .message("Se requiere autenticación completa para acceder a este recurso")
                .path(request.getRequestURI())
                .build();

        objectMapper.findAndRegisterModules();
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
