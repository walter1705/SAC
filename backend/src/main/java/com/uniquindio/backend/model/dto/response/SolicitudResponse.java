package com.uniquindio.backend.dto.response;

import com.uniquindio.backend.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SolicitudResponse {

    private Long id;
    private TipoSolicitud tipo;
    private String descripcion;
    private CanalOrigen canalOrigen;
    private Instant fechaHoraRegistro;
    private String idSolicitante;
    private Prioridad prioridad;
    private String justificacionPrioridad;
    private EstadoSolicitud estado;
    private String observacionCierre;
}
