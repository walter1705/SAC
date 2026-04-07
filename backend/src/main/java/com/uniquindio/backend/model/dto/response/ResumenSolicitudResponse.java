package com.uniquindio.backend.model.dto.response;

import java.time.Instant;

public record ResumenSolicitudResponse(
        Long idSolicitud,
        String resumen,
        Instant generadoEn
) {}
