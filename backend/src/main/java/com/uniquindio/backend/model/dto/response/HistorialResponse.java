package com.uniquindio.backend.model.dto.response;

import java.time.Instant;

public record HistorialResponse(
        Long id,
        Instant fechaHora,
        String accion,
        String usuarioResponsable,
        String observaciones
) {}
