package com.uniquindio.backend.dto.request;

import com.uniquindio.backend.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambiarEstadoRequest {

    @NotNull(message = "El nuevo estado es requerido")
    private EstadoSolicitud nuevoEstado;

    @Size(max = 1000, message = "La observación no puede exceder 1000 caracteres")
    private String observacion;
}
