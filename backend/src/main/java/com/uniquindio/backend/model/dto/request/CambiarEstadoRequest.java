package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.EstadoSolicitud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambiarEstadoRequest {

    @NotNull(message = "El nuevo estado es requerido")
    private EstadoSolicitud nuevoEstado;

    @Size(max = 1000, message = "La nota no puede exceder 1000 caracteres")
    private String nota;
}
