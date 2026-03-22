package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AsignarResponsableRequest {

    @NotNull(message = "El ID del responsable es requerido")
    private Long responsableId;

    @Size(max = 500, message = "La nota de asignación no puede exceder 500 caracteres")
    private String notaAsignacion;
}
