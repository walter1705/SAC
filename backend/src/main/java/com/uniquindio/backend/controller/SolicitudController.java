package com.uniquindio.backend.controller;

import com.uniquindio.backend.model.Usuario;
import com.uniquindio.backend.model.dto.request.*;
import com.uniquindio.backend.model.dto.response.*;
import com.uniquindio.backend.model.enums.*;
import com.uniquindio.backend.service.SolicitudService;
import com.uniquindio.backend.util.exception.ForbiddenException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    private static final Set<String> SORT_FIELDS_PERMITIDOS =
            Set.of("fechaHoraRegistro", "estado", "prioridad", "tipo");

    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(
            @Valid @RequestBody CrearSolicitudRequest request,
            Authentication authentication) {
        String nombreUsuario = getNombreUsuario(authentication);
        SolicitudResponse response = solicitudService.crearSolicitud(request, nombreUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<SolicitudesPaginadasResponse> listarSolicitudes(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) TipoSolicitud tipo,
            @RequestParam(required = false) Prioridad prioridad,
            @RequestParam(required = false) Long responsable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaHoraRegistro,desc") String sort) {

        Pageable pageable = createPageable(page, size, sort);
        SolicitudesPaginadasResponse response = solicitudService.listarSolicitudes(
                estado, tipo, prioridad, responsable, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerSolicitudPorId(@PathVariable Long id) {
        SolicitudResponse response = solicitudService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/clasificar")
    public ResponseEntity<SolicitudResponse> clasificarSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody ClasificarSolicitudRequest request,
            Authentication authentication) {
        String nombreUsuario = getNombreUsuario(authentication);
        SolicitudResponse response = solicitudService.clasificarSolicitud(id, request, nombreUsuario);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudResponse> cambiarEstadoSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequest request,
            Authentication authentication) {
        String nombreUsuario = getNombreUsuario(authentication);
        SolicitudResponse response = solicitudService.cambiarEstado(id, request, nombreUsuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/asignar")
    public ResponseEntity<AsignacionResponse> asignarResponsable(
            @PathVariable Long id,
            @Valid @RequestBody AsignarResponsableRequest request,
            Authentication authentication) {
        String nombreUsuario = getNombreUsuario(authentication);
        AsignacionResponse response = solicitudService.asignarResponsable(id, request, nombreUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudResponse> cerrarSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody CerrarSolicitudRequest request,
            Authentication authentication) {
        String nombreUsuario = getNombreUsuario(authentication);
        SolicitudResponse response = solicitudService.cerrarSolicitud(id, request, nombreUsuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialResponse>> obtenerHistorial(@PathVariable Long id) {
        List<HistorialResponse> historial = solicitudService.obtenerHistorial(id);
        return ResponseEntity.ok(historial);
    }

    private String getNombreUsuario(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            return usuario.getNombreUsuario();
        }
        throw new ForbiddenException("No se pudo determinar la identidad del usuario autenticado");
    }

    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        if (!SORT_FIELDS_PERMITIDOS.contains(sortField)) {
            sortField = "fechaHoraRegistro";
        }
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        int clampedSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(page, clampedSize, Sort.by(direction, sortField));
    }
}
