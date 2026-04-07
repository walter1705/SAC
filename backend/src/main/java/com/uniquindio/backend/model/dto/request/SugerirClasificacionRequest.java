package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SugerirClasificacionRequest(

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
        String descripcion
) {}
