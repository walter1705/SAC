package com.uniquindio.backend.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

import com.uniquindio.backend.model.enums.*;

@Data
@Builder
public class SolicitudResponse {

    private Long id;
    private String estudianteNombre;
    private String estudianteCorreo;
    private String estudianteTelefono;
    private String estudianteIdentificacion;
    private String asunto;
    private String descripcion;
    private CanalOrigen canalOrigen;
    private Instant fechaHoraRegistro;
    private TipoSolicitud tipo;
    private Prioridad prioridad;
    private String notaClasificacion;
    private EstadoSolicitud estado;
    private String resolucion;
    private String notasCierre;
}
