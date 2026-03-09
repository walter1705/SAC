package com.uniquindio.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarResponsableRequest {

    @NotNull(message = "El ID del usuario es requerido")
    private Long usuarioId;
}
