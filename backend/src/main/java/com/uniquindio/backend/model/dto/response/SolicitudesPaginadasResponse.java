package com.uniquindio.backend.model.dto.response;

import java.util.List;

public record SolicitudesPaginadasResponse(
        List<SolicitudResponse> content,
        Integer pagina,
        Integer tamaño,
        Long totalElementos,
        Integer totalPaginas
) {}
