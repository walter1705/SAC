package com.uniquindio.backend.service;

import com.uniquindio.backend.model.*;
import com.uniquindio.backend.model.dto.request.*;
import com.uniquindio.backend.model.dto.response.*;
import com.uniquindio.backend.model.enums.*;
import com.uniquindio.backend.repository.*;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final AsignacionRepository asignacionRepository;
    private final HistorialRepository historialRepository;
    private final UsuarioService usuarioService;

    // Valid state transitions map
    private static final Map<EstadoSolicitud, Set<EstadoSolicitud>> VALID_TRANSITIONS = Map.of(
            EstadoSolicitud.REGISTRADA, Set.of(EstadoSolicitud.CLASIFICADA),
            EstadoSolicitud.CLASIFICADA, Set.of(EstadoSolicitud.EN_ATENCION),
            EstadoSolicitud.EN_ATENCION, Set.of(EstadoSolicitud.ATENDIDA),
            EstadoSolicitud.ATENDIDA, Set.of(EstadoSolicitud.CERRADA),
            EstadoSolicitud.CERRADA, Set.of()
    );

    @Transactional
    public SolicitudResponse crearSolicitud(CrearSolicitudRequest request, String username) {
        Solicitud solicitud = Solicitud.builder()
                .tipo(request.getTipo())
                .descripcion(request.getDescripcion())
                .canalOrigen(request.getCanalOrigen())
                .idSolicitante(request.getIdSolicitante())
                .estado(EstadoSolicitud.REGISTRADA)
                .build();

        Solicitud saved = solicitudRepository.save(solicitud);

        // Create audit history entry
        registrarHistorial(saved, "REGISTRO", username, "Solicitud creada vía " + request.getCanalOrigen());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SolicitudesPaginadasResponse listarSolicitudes(
            EstadoSolicitud estado,
            TipoSolicitud tipo,
            Prioridad prioridad,
            Long responsableId,
            Pageable pageable) {

        Page<Solicitud> page = solicitudRepository.findWithFilters(estado, tipo, prioridad, responsableId, pageable);

        List<SolicitudResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return SolicitudesPaginadasResponse.builder()
                .content(content)
                .pagina(page.getNumber())
                .tamaño(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorId(Long id) {
        Solicitud solicitud = findSolicitudById(id);
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudResponse clasificarSolicitud(Long id, ClasificarSolicitudRequest request, String username) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BadRequestException("No se puede modificar una solicitud cerrada");
        }

        if (solicitud.getEstado() != EstadoSolicitud.REGISTRADA) {
            throw new BadRequestException("Solo se pueden clasificar solicitudes en estado REGISTRADA");
        }

        solicitud.setTipo(request.getTipo());
        solicitud.setPrioridad(request.getPrioridad());
        solicitud.setJustificacionPrioridad(request.getJustificacionPrioridad());
        solicitud.setEstado(EstadoSolicitud.CLASIFICADA);

        Solicitud saved = solicitudRepository.save(solicitud);

        registrarHistorial(saved, "CLASIFICACION", username,
                "Clasificada como " + request.getTipo() + " con prioridad " + request.getPrioridad());

        return toResponse(saved);
    }

    @Transactional
    public SolicitudResponse cambiarEstado(Long id, CambiarEstadoRequest request, String username) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BadRequestException("No se puede modificar una solicitud cerrada");
        }

        Set<EstadoSolicitud> validNextStates = VALID_TRANSITIONS.get(solicitud.getEstado());
        if (!validNextStates.contains(request.getNuevoEstado())) {
            throw new BadRequestException(
                    "Transición de estado inválida de " + solicitud.getEstado() + " a " + request.getNuevoEstado());
        }

        solicitud.setEstado(request.getNuevoEstado());
        Solicitud saved = solicitudRepository.save(solicitud);

        String observacion = request.getObservacion() != null ? request.getObservacion() : 
                "Estado cambiado a " + request.getNuevoEstado();
        registrarHistorial(saved, "CAMBIO_ESTADO", username, observacion);

        return toResponse(saved);
    }

    @Transactional
    public AsignacionResponse asignarResponsable(Long solicitudId, AsignarResponsableRequest request, String username) {
        Solicitud solicitud = findSolicitudById(solicitudId);

        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BadRequestException("No se puede asignar responsable a una solicitud cerrada");
        }

        Usuario usuario = usuarioService.findById(request.getUsuarioId());

        if (!usuario.getActivo()) {
            throw new BadRequestException("No se puede asignar un usuario inactivo");
        }

        // Deactivate existing assignments
        asignacionRepository.findBySolicitudIdAndActivaTrue(solicitudId)
                .forEach(a -> {
                    a.setActiva(false);
                    asignacionRepository.save(a);
                });

        Asignacion asignacion = Asignacion.builder()
                .solicitud(solicitud)
                .usuario(usuario)
                .activa(true)
                .build();

        Asignacion saved = asignacionRepository.save(asignacion);

        registrarHistorial(solicitud, "ASIGNACION", username, "Asignada a " + usuario.getUsername() + " para gestión");

        return AsignacionResponse.builder()
                .id(saved.getId())
                .solicitudId(solicitud.getId())
                .usuarioId(usuario.getId())
                .fechaAsignacion(saved.getFechaAsignacion())
                .activa(saved.getActiva())
                .build();
    }

    @Transactional
    public SolicitudResponse cerrarSolicitud(Long id, CerrarSolicitudRequest request, String username) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() != EstadoSolicitud.ATENDIDA) {
            throw new BadRequestException(
                    "No se puede cerrar la solicitud – estado actual es " + solicitud.getEstado() + ", se esperaba ATENDIDA");
        }

        solicitud.setEstado(EstadoSolicitud.CERRADA);
        solicitud.setObservacionCierre(request.getObservacionCierre());

        Solicitud saved = solicitudRepository.save(solicitud);

        registrarHistorial(saved, "CIERRE", username, request.getObservacionCierre());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<HistorialResponse> obtenerHistorial(Long solicitudId) {
        // Verify the solicitud exists
        findSolicitudById(solicitudId);

        return historialRepository.findBySolicitudIdOrderByFechaHoraAsc(solicitudId).stream()
                .map(this::toHistorialResponse)
                .collect(Collectors.toList());
    }

    private Solicitud findSolicitudById(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud con ID " + id + " no encontrada"));
    }

    private void registrarHistorial(Solicitud solicitud, String accion, String username, String observaciones) {
        Historial historial = Historial.builder()
                .solicitud(solicitud)
                .accion(accion)
                .usuarioResponsable(username)
                .observaciones(observaciones)
                .build();
        historialRepository.save(historial);
    }

    private SolicitudResponse toResponse(Solicitud solicitud) {
        return SolicitudResponse.builder()
                .id(solicitud.getId())
                .tipo(solicitud.getTipo())
                .descripcion(solicitud.getDescripcion())
                .canalOrigen(solicitud.getCanalOrigen())
                .fechaHoraRegistro(solicitud.getFechaHoraRegistro())
                .idSolicitante(solicitud.getIdSolicitante())
                .prioridad(solicitud.getPrioridad())
                .justificacionPrioridad(solicitud.getJustificacionPrioridad())
                .estado(solicitud.getEstado())
                .observacionCierre(solicitud.getObservacionCierre())
                .build();
    }

    private HistorialResponse toHistorialResponse(Historial historial) {
        return HistorialResponse.builder()
                .id(historial.getId())
                .fechaHora(historial.getFechaHora())
                .accion(historial.getAccion())
                .usuarioResponsable(historial.getUsuarioResponsable())
                .observaciones(historial.getObservaciones())
                .build();
    }
}
