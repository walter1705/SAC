package com.uniquindio.backend.model.dto.response;

import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SugerirClasificacionResponse {

    private TipoSolicitud tipoSugerido;
    private Prioridad prioridadSugerida;
    private String justificacion;
    private Float confianza;
}
