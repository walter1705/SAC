package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CerrarSolicitudRequest(

        @NotBlank(message = "La resolución es requerida")
        @Size(min = 10, max = 2000, message = "La resolución debe tener entre 10 y 2000 caracteres")
        String resolucion,

        @Size(max = 1000, message = "Las notas de cierre no pueden exceder 1000 caracteres")
        String notasCierre
) {}
