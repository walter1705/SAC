package com.uniquindio.backend.controller;

import com.uniquindio.backend.model.dto.request.SugerirClasificacionRequest;
import com.uniquindio.backend.model.dto.response.ResumenSolicitudResponse;
import com.uniquindio.backend.model.dto.response.SugerirClasificacionResponse;
import com.uniquindio.backend.service.IAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ia")
@RequiredArgsConstructor
public class IAController {

    private final IAService iaService;

    @PostMapping("/sugerir-clasificacion")
    public ResponseEntity<SugerirClasificacionResponse> sugerirClasificacion(
            @Valid @RequestBody SugerirClasificacionRequest request) {
        SugerirClasificacionResponse response = iaService.sugerirClasificacion(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/solicitudes/{id}/resumen")
    public ResponseEntity<ResumenSolicitudResponse> generarResumen(@PathVariable Long id) {
        ResumenSolicitudResponse response = iaService.generarResumen(id);
        return ResponseEntity.ok(response);
    }
}
