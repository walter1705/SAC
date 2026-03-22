package com.uniquindio.backend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenSolicitudResponse {

    private Long idSolicitud;
    private String resumen;
    private Instant generadoEn;
}
