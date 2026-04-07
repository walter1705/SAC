package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.CanalOrigen;
import com.uniquindio.backend.model.enums.EstadoSolicitud;
import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;

import java.time.Instant;

public record SolicitudResponse(
        Long id,
        String estudianteNombre,
        String estudianteCorreo,
        String estudianteTelefono,
        String estudianteIdentificacion,
        String asunto,
        String descripcion,
        CanalOrigen canalOrigen,
        Instant fechaHoraRegistro,
        TipoSolicitud tipo,
        Prioridad prioridad,
        String notaClasificacion,
        EstadoSolicitud estado,
        String resolucion,
        String notasCierre
) {}
