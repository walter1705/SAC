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

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final AsignacionRepository asignacionRepository;
    private final HistorialRepository historialRepository;
    private final UsuarioService usuarioService;

    private static final Map<EstadoSolicitud, Set<EstadoSolicitud>> VALID_TRANSITIONS = Map.of(
            EstadoSolicitud.REGISTRADA, Set.of(EstadoSolicitud.CLASIFICADA),
            EstadoSolicitud.CLASIFICADA, Set.of(EstadoSolicitud.EN_ATENCION),
            EstadoSolicitud.EN_ATENCION, Set.of(EstadoSolicitud.ATENDIDA),
            EstadoSolicitud.ATENDIDA, Set.of(EstadoSolicitud.CERRADA),
            EstadoSolicitud.CERRADA, Set.of()
    );

    @Transactional
    public SolicitudResponse crearSolicitud(CrearSolicitudRequest request, String nombreUsuario) {
        Solicitud solicitud = Solicitud.builder()
                .estudianteNombre(request.estudianteNombre())
                .estudianteCorreo(request.estudianteCorreo())
                .estudianteTelefono(request.estudianteTelefono())
                .estudianteIdentificacion(request.estudianteIdentificacion())
                .asunto(request.asunto())
                .descripcion(request.descripcion())
                .canalOrigen(request.canalOrigen())
                .estado(EstadoSolicitud.REGISTRADA)
                .build();

        Solicitud saved = solicitudRepository.save(solicitud);

        registrarHistorial(saved, AccionHistorial.REGISTRO, nombreUsuario, "Solicitud creada vía " + request.canalOrigen());

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
                .toList();

        return new SolicitudesPaginadasResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorId(Long id) {
        Solicitud solicitud = findSolicitudById(id);
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudResponse clasificarSolicitud(Long id, ClasificarSolicitudRequest request, String nombreUsuario) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() != EstadoSolicitud.REGISTRADA) {
            String mensaje = solicitud.getEstado() == EstadoSolicitud.CERRADA
                    ? "No se puede modificar una solicitud cerrada"
                    : "Solo se pueden clasificar solicitudes en estado REGISTRADA";
            throw new BadRequestException(mensaje);
        }

        solicitud.setTipo(request.tipo());
        solicitud.setPrioridad(request.prioridad());
        solicitud.setNotaClasificacion(request.notaClasificacion());
        solicitud.setEstado(EstadoSolicitud.CLASIFICADA);

        Solicitud saved = solicitudRepository.save(solicitud);

        registrarHistorial(saved, AccionHistorial.CLASIFICACION, nombreUsuario,
                "Clasificada como " + request.tipo() + " con prioridad " + request.prioridad());

        return toResponse(saved);
    }

    @Transactional
    public SolicitudResponse cambiarEstado(Long id, CambiarEstadoRequest request, String nombreUsuario) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BadRequestException("No se puede modificar una solicitud cerrada");
        }

        Set<EstadoSolicitud> validNextStates = VALID_TRANSITIONS.get(solicitud.getEstado());
        if (!validNextStates.contains(request.nuevoEstado())) {
            throw new BadRequestException(
                    "Transición de estado inválida de " + solicitud.getEstado() + " a " + request.nuevoEstado());
        }

        solicitud.setEstado(request.nuevoEstado());
        Solicitud saved = solicitudRepository.save(solicitud);

        String nota = request.nota() != null ? request.nota() :
                "Estado cambiado a " + request.nuevoEstado();
        registrarHistorial(saved, AccionHistorial.CAMBIO_ESTADO, nombreUsuario, nota);

        return toResponse(saved);
    }

    @Transactional
    public AsignacionResponse asignarResponsable(Long solicitudId, AsignarResponsableRequest request, String nombreUsuario) {
        Solicitud solicitud = findSolicitudById(solicitudId);

        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BadRequestException("No se puede asignar responsable a una solicitud cerrada");
        }

        Usuario usuario = usuarioService.findById(request.responsableId());

        if (!usuario.getActivo()) {
            throw new BadRequestException("No se puede asignar un usuario inactivo");
        }

        if (usuario.getRol() != RolUsuario.GESTOR) {
            throw new BadRequestException("Solo se puede asignar como responsable a un usuario con rol GESTOR");
        }

        List<Asignacion> asignacionesActivas = asignacionRepository.findBySolicitudIdAndActivaTrue(solicitudId);
        asignacionesActivas.forEach(a -> a.setActiva(false));
        asignacionRepository.saveAll(asignacionesActivas);

        Asignacion asignacion = Asignacion.builder()
                .solicitud(solicitud)
                .usuario(usuario)
                .activa(true)
                .build();

        Asignacion saved = asignacionRepository.save(asignacion);

        String mensajeHistorial = request.notaAsignacion() != null
                ? request.notaAsignacion()
                : "Asignada a " + usuario.getNombreUsuario() + " para gestión";
        registrarHistorial(solicitud, AccionHistorial.ASIGNACION, nombreUsuario, mensajeHistorial);

        return new AsignacionResponse(
                saved.getId(),
                solicitud.getId(),
                usuario.getId(),
                saved.getFechaAsignacion(),
                saved.getActiva()
        );
    }

    @Transactional
    public SolicitudResponse cerrarSolicitud(Long id, CerrarSolicitudRequest request, String nombreUsuario) {
        Solicitud solicitud = findSolicitudById(id);

        if (solicitud.getEstado() != EstadoSolicitud.ATENDIDA) {
            throw new BadRequestException(
                    "No se puede cerrar la solicitud – estado actual es " + solicitud.getEstado() + ", se esperaba ATENDIDA");
        }

        solicitud.setEstado(EstadoSolicitud.CERRADA);
        solicitud.setResolucion(request.resolucion());
        solicitud.setNotasCierre(request.notasCierre());

        Solicitud saved = solicitudRepository.save(solicitud);

        String mensajeHistorial = request.resolucion();
        if (request.notasCierre() != null) {
            mensajeHistorial += " - " + request.notasCierre();
        }
        registrarHistorial(saved, AccionHistorial.CIERRE, nombreUsuario, mensajeHistorial);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<HistorialResponse> obtenerHistorial(Long solicitudId) {
        findSolicitudById(solicitudId);

        return historialRepository.findBySolicitudIdOrderByFechaHoraAsc(solicitudId).stream()
                .map(this::toHistorialResponse)
                .toList();
    }

    private Solicitud findSolicitudById(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud con ID " + id + " no encontrada"));
    }

    private void registrarHistorial(Solicitud solicitud, AccionHistorial accion, String nombreUsuario, String observaciones) {
        Historial historial = Historial.builder()
                .solicitud(solicitud)
                .accion(accion)
                .usuarioResponsable(nombreUsuario)
                .observaciones(observaciones)
                .build();
        historialRepository.save(historial);
    }

    private SolicitudResponse toResponse(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getEstudianteNombre(),
                solicitud.getEstudianteCorreo(),
                solicitud.getEstudianteTelefono(),
                solicitud.getEstudianteIdentificacion(),
                solicitud.getAsunto(),
                solicitud.getDescripcion(),
                solicitud.getCanalOrigen(),
                solicitud.getFechaHoraRegistro(),
                solicitud.getTipo(),
                solicitud.getPrioridad(),
                solicitud.getNotaClasificacion(),
                solicitud.getEstado(),
                solicitud.getResolucion(),
                solicitud.getNotasCierre()
        );
    }

    private HistorialResponse toHistorialResponse(Historial historial) {
        return new HistorialResponse(
                historial.getId(),
                historial.getFechaHora(),
                historial.getAccion().name(),
                historial.getUsuarioResponsable(),
                historial.getObservaciones()
        );
    }
}
