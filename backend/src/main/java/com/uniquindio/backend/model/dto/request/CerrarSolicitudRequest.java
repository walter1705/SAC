package com.uniquindio.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CerrarSolicitudRequest {

    @NotBlank(message = "La observación de cierre es requerida")
    @Size(min = 10, max = 2000, message = "La observación de cierre debe tener entre 10 y 2000 caracteres")
    private String observacionCierre;
}
