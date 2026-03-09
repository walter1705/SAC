package com.uniquindio.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class HistorialResponse {

    private Long id;
    private Instant fechaHora;
    private String accion;
    private String usuarioResponsable;
    private String observaciones;
}
