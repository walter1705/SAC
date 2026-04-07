package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoUsuarioRequest(

        @NotNull(message = "El estado activo es requerido")
        Boolean activo
) {}
