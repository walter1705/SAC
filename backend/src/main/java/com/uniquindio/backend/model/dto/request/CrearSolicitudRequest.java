package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.CanalOrigen;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(

        @NotBlank(message = "El nombre del solicitante es requerido")
        @Size(min = 2, max = 100, message = "El nombre del solicitante debe tener entre 2 y 100 caracteres")
        String solicitanteNombre,

        @NotBlank(message = "El correo del solicitante es requerido")
        @Email(message = "El correo del solicitante debe ser válido")
        @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
        String solicitanteCorreo,

        @NotBlank(message = "El teléfono del solicitante es requerido")
        @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres")
        @Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "El teléfono solo puede contener dígitos, +, -, espacios y paréntesis")
        String solicitanteTelefono,

        @NotBlank(message = "La identificación del solicitante es requerida")
        @Size(min = 1, max = 50, message = "La identificación debe tener entre 1 y 50 caracteres")
        String solicitanteIdentificacion,

        @NotBlank(message = "El asunto es requerido")
        @Size(min = 5, max = 200, message = "El asunto debe tener entre 5 y 200 caracteres")
        String asunto,

        @NotBlank(message = "La descripción es requerida")
        @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
        String descripcion,

        @NotNull(message = "El canal de origen es requerido")
        CanalOrigen canalOrigen
) {}
