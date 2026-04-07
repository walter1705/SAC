package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClasificarSolicitudRequest(

        @NotNull(message = "El tipo de solicitud es requerido")
        TipoSolicitud tipo,

        @NotNull(message = "La prioridad es requerida")
        Prioridad prioridad,

        @NotBlank(message = "La nota de clasificación es requerida")
        @Size(min = 5, max = 500, message = "La nota de clasificación debe tener entre 5 y 500 caracteres")
        String notaClasificacion
) {}
