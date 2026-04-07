package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.CanalOrigen;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(

        @NotBlank(message = "El nombre del estudiante es requerido")
        @Size(min = 2, max = 100, message = "El nombre del estudiante debe tener entre 2 y 100 caracteres")
        String estudianteNombre,

        @NotBlank(message = "El correo del estudiante es requerido")
        @Email(message = "El correo del estudiante debe ser válido")
        @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
        String estudianteCorreo,

        @NotBlank(message = "El teléfono del estudiante es requerido")
        @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres")
        String estudianteTelefono,

        @NotBlank(message = "La identificación del estudiante es requerida")
        @Size(min = 1, max = 50, message = "La identificación debe tener entre 1 y 50 caracteres")
        String estudianteIdentificacion,

        @NotBlank(message = "El asunto es requerido")
        @Size(min = 5, max = 200, message = "El asunto debe tener entre 5 y 200 caracteres")
        String asunto,

        @NotBlank(message = "La descripción es requerida")
        @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
        String descripcion,

        @NotNull(message = "El canal de origen es requerido")
        CanalOrigen canalOrigen
) {}
