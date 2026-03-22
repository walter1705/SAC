package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CerrarSolicitudRequest {

    @NotBlank(message = "La resolución es requerida")
    @Size(min = 10, max = 2000, message = "La resolución debe tener entre 10 y 2000 caracteres")
    private String resolucion;

    @Size(max = 1000, message = "Las notas de cierre no pueden exceder 1000 caracteres")
    private String notasCierre;
}
