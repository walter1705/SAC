package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;

public record SugerirClasificacionResponse(
        TipoSolicitud tipoSugerido,
        Prioridad prioridadSugerida,
        String justificacion,
        Float confianza
) {}
