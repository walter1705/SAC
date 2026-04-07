package com.uniquindio.backend.model.dto.response;

import java.time.Instant;

public record AsignacionResponse(
        Long id,
        Long solicitudId,
        Long usuarioId,
        Instant fechaAsignacion,
        Boolean activa
) {}
