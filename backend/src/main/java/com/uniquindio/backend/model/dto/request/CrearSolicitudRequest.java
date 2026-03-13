package com.uniquindio.backend.model.dto.request;

import com.uniquindio.backend.model.enums.CanalOrigen;
import com.uniquindio.backend.model.enums.TipoSolicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearSolicitudRequest {

    @NotNull(message = "El tipo de solicitud es requerido")
    private TipoSolicitud tipo;

    @NotBlank(message = "La descripción es requerida")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    private String descripcion;

    @NotNull(message = "El canal de origen es requerido")
    private CanalOrigen canalOrigen;

    @NotBlank(message = "El ID del solicitante es requerido")
    @Size(min = 1, max = 50, message = "El ID del solicitante debe tener entre 1 y 50 caracteres")
    private String idSolicitante;
}
