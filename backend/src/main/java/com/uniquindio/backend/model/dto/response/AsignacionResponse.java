package com.uniquindio.backend.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AsignacionResponse {

    private Long id;
    private Long solicitudId;
    private Long usuarioId;
    private Instant fechaAsignacion;
    private Boolean activa;
}
