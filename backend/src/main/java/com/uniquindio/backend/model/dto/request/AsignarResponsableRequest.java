package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AsignarResponsableRequest(

        @NotNull(message = "El ID del responsable es requerido")
        Long responsableId,

        @Size(max = 500, message = "La nota de asignación no puede exceder 500 caracteres")
        String notaAsignacion
) {}
