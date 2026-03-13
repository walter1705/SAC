package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClasificarSolicitudRequest {

    @NotNull(message = "El tipo de solicitud es requerido")
    private TipoSolicitud tipo;

    @NotNull(message = "La prioridad es requerida")
    private Prioridad prioridad;

    @NotBlank(message = "La justificación de prioridad es requerida")
    @Size(min = 5, max = 500, message = "La justificación debe tener entre 5 y 500 caracteres")
    private String justificacionPrioridad;
}
