package com.uniquindio.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SolicitudesPaginadasResponse {

    private List<SolicitudResponse> content;
    private Integer pagina;
    private Integer tamaño;
    private Long totalElementos;
    private Integer totalPaginas;
}
