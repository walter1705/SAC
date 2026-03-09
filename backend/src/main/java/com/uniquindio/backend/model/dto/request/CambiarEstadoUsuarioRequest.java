package com.uniquindio.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoUsuarioRequest {

    @NotNull(message = "El estado activo es requerido")
    private Boolean activo;
}
