package com.uniquindio.backend.service;

import com.uniquindio.backend.model.*;
import com.uniquindio.backend.model.dto.request.*;
import com.uniquindio.backend.model.dto.response.*;
import com.uniquindio.backend.model.enums.*;
import static org.mockito.ArgumentMatchers.anyList;
import com.uniquindio.backend.repository.*;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private SolicitudService solicitudService;

    private Solicitud testSolicitud;
    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        testSolicitud = Solicitud.builder()
                .id(1L)
                .solicitanteNombre("Pedro Solicitante")
                .solicitanteCorreo("pedro@uni.edu.co")
                .solicitanteTelefono("3001234567")
                .solicitanteIdentificacion("12345678")
                .asunto("Solicitud de homologacion")
                .descripcion("Necesito homologar materias del programa anterior")
                .canalOrigen(CanalOrigen.WEB)
                .fechaHoraRegistro(Instant.now())
                .estado(EstadoSolicitud.REGISTRADA)
                .build();

        testUsuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Juan Gestor")
                .nombreUsuario("jgestor")
                .email("jgestor@uni.edu.co")
                .rol(RolUsuario.GESTOR)
                .activo(true)
                .build();
    }

    @Nested
    @DisplayName("Tests de Crear Solicitud")
    class CrearSolicitudTests {

        @Test
        @DisplayName("Crea solicitud exitosamente")
        void crearSolicitud_conDatosValidos_retornaSolicitudResponse() {
            CrearSolicitudRequest request = new CrearSolicitudRequest(
                    "Pedro Solicitante",
                    "pedro@uni.edu.co",
                    "3001234567",
                    "12345678",
                    "Solicitud de homologacion",
                    "Necesito homologar materias del programa anterior",
                    CanalOrigen.WEB
            );

            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> {
                Solicitud saved = invocation.getArgument(0);
                saved.setId(1L);
                saved.setFechaHoraRegistro(Instant.now());
                return saved;
            });

            SolicitudResponse response = solicitudService.crearSolicitud(request, "admin");

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.solicitanteNombre()).isEqualTo("Pedro Solicitante");
            assertThat(response.estado()).isEqualTo(EstadoSolicitud.REGISTRADA);

            verify(solicitudRepository).save(any(Solicitud.class));
            verify(historialRepository).save(any(Historial.class));
        }
    }

    @Nested
    @DisplayName("Tests de Listar Solicitudes")
    class ListarSolicitudesTests {

        @Test
        @DisplayName("Lista solicitudes con filtros")
        void listarSolicitudes_conFiltros_retornaPaginaFiltrada() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Solicitud> page = new PageImpl<>(List.of(testSolicitud), pageable, 1);

            when(solicitudRepository.findWithFilters(
                    eq(EstadoSolicitud.REGISTRADA), isNull(), isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(page);

            SolicitudesPaginadasResponse response = solicitudService.listarSolicitudes(
                    EstadoSolicitud.REGISTRADA, null, null, null, pageable);

            assertThat(response).isNotNull();
            assertThat(response.content()).hasSize(1);
            assertThat(response.totalElementos()).isEqualTo(1);
            assertThat(response.pagina()).isEqualTo(0);
        }

        @Test
        @DisplayName("Lista solicitudes sin filtros")
        void listarSolicitudes_sinFiltros_retornaTodasLasSolicitudes() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Solicitud> page = new PageImpl<>(List.of(testSolicitud), pageable, 1);

            when(solicitudRepository.findWithFilters(isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                    .thenReturn(page);

            SolicitudesPaginadasResponse response = solicitudService.listarSolicitudes(
                    null, null, null, null, pageable);

            assertThat(response).isNotNull();
            assertThat(response.content()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Tests de Obtener Solicitud por ID")
    class ObtenerPorIdTests {

        @Test
        @DisplayName("Obtiene solicitud por ID existente")
        void obtenerPorId_conIdValido_retornaSolicitudResponse() {
            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            SolicitudResponse response = solicitudService.obtenerPorId(1L);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.solicitanteNombre()).isEqualTo("Pedro Solicitante");
        }

        @Test
        @DisplayName("Falla al obtener solicitud por ID no existente")
        void obtenerPorId_conIdInvalido_lanzaResourceNotFoundException() {
            when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> solicitudService.obtenerPorId(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Solicitud con ID 999 no encontrada");
        }
    }

    @Nested
    @DisplayName("Tests de Clasificar Solicitud")
    class ClasificarSolicitudTests {

        @Test
        @DisplayName("Clasifica solicitud exitosamente")
        void clasificarSolicitud_conEstadoRegistrada_retornaSolicitudClasificada() {
            ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(
                    TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, "Solicitud prioritaria de homologacion"
            );

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.clasificarSolicitud(1L, request, "admin");

            assertThat(response).isNotNull();
            assertThat(response.estado()).isEqualTo(EstadoSolicitud.CLASIFICADA);
            assertThat(response.tipo()).isEqualTo(TipoSolicitud.HOMOLOGACION);
            assertThat(response.prioridad()).isEqualTo(Prioridad.ALTA);

            verify(historialRepository).save(any(Historial.class));
        }

        @Test
        @DisplayName("Falla al clasificar solicitud no en estado REGISTRADA")
        void clasificarSolicitud_conEstadoNoRegistrada_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.CLASIFICADA);
            ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(
                    TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, "Nota de clasificacion"
            );

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            assertThatThrownBy(() -> solicitudService.clasificarSolicitud(1L, request, "admin"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Solo se pueden clasificar solicitudes en estado REGISTRADA");
        }

        @Test
        @DisplayName("Falla al clasificar solicitud cerrada")
        void clasificarSolicitud_conEstadoCerrada_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.CERRADA);
            ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(
                    TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, "Nota de clasificacion"
            );

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            // CERRADA != REGISTRADA, el mensaje es específico para el estado CERRADA
            assertThatThrownBy(() -> solicitudService.clasificarSolicitud(1L, request, "admin"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("No se puede modificar una solicitud cerrada");
        }
    }

    @Nested
    @DisplayName("Tests de Cambiar Estado")
    class CambiarEstadoTests {

        @Test
        @DisplayName("Cambia estado de CLASIFICADA a EN_ATENCION exitosamente")
        void cambiarEstado_transicionValida_retornaSolicitudConNuevoEstado() {
            testSolicitud.setEstado(EstadoSolicitud.CLASIFICADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.EN_ATENCION, "Iniciando atencion");

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cambiarEstado(1L, request, "gestor");

            assertThat(response).isNotNull();
            assertThat(response.estado()).isEqualTo(EstadoSolicitud.EN_ATENCION);

            verify(historialRepository).save(any(Historial.class));
        }

        @Test
        @DisplayName("Falla con transicion de estado invalida")
        void cambiarEstado_transicionInvalida_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.REGISTRADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.CERRADA, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            assertThatThrownBy(() -> solicitudService.cambiarEstado(1L, request, "gestor"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("Transición de estado inválida");
        }

        @Test
        @DisplayName("Falla al cambiar estado de solicitud cerrada")
        void cambiarEstado_solicitudCerrada_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.CERRADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.EN_ATENCION, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            assertThatThrownBy(() -> solicitudService.cambiarEstado(1L, request, "gestor"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("No se puede modificar una solicitud cerrada");
        }

        @Test
        @DisplayName("Cambia estado con nota por defecto cuando no se proporciona")
        void cambiarEstado_sinNota_usaNotaPorDefecto() {
            testSolicitud.setEstado(EstadoSolicitud.CLASIFICADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.EN_ATENCION, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            solicitudService.cambiarEstado(1L, request, "gestor");

            verify(historialRepository).save(argThat(historial ->
                    historial.getObservaciones().contains("Estado cambiado a EN_ATENCION")));
        }
    }

    @Nested
    @DisplayName("Tests de Asignar Responsable")
    class AsignarResponsableTests {

        @Test
        @DisplayName("Asigna responsable exitosamente")
        void asignarResponsable_conDatosValidos_retornaAsignacionResponse() {
            AsignarResponsableRequest request = new AsignarResponsableRequest(1L, "Asignacion urgente");

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(usuarioService.findById(1L)).thenReturn(testUsuario);
            when(asignacionRepository.findBySolicitudIdAndActivaTrue(1L)).thenReturn(List.of());
            when(asignacionRepository.save(any(Asignacion.class))).thenAnswer(invocation -> {
                Asignacion saved = invocation.getArgument(0);
                saved.setId(1L);
                saved.setFechaAsignacion(Instant.now());
                return saved;
            });

            AsignacionResponse response = solicitudService.asignarResponsable(1L, request, "admin");

            assertThat(response).isNotNull();
            assertThat(response.solicitudId()).isEqualTo(1L);
            assertThat(response.usuarioId()).isEqualTo(1L);
            assertThat(response.activa()).isTrue();
        }

        @Test
        @DisplayName("Desactiva asignaciones anteriores al asignar nuevo responsable")
        void asignarResponsable_conAsignacionExistente_desactivaAnterior() {
            Asignacion asignacionAnterior = Asignacion.builder()
                    .id(1L)
                    .solicitud(testSolicitud)
                    .usuario(testUsuario)
                    .activa(true)
                    .build();

            AsignarResponsableRequest request = new AsignarResponsableRequest(2L, null);

            Usuario nuevoUsuario = Usuario.builder()
                    .id(2L)
                    .nombreUsuario("nuevo")
                    .rol(RolUsuario.GESTOR)
                    .activo(true)
                    .build();

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(usuarioService.findById(2L)).thenReturn(nuevoUsuario);
            when(asignacionRepository.findBySolicitudIdAndActivaTrue(1L)).thenReturn(List.of(asignacionAnterior));
            when(asignacionRepository.save(any(Asignacion.class))).thenAnswer(invocation -> {
                Asignacion saved = invocation.getArgument(0);
                if (saved.getId() == null) {
                    saved.setId(2L);
                    saved.setFechaAsignacion(Instant.now());
                }
                return saved;
            });

            solicitudService.asignarResponsable(1L, request, "admin");

            verify(asignacionRepository).saveAll(anyList());
            verify(asignacionRepository).save(any(Asignacion.class));
        }

        @Test
        @DisplayName("Falla al asignar usuario inactivo")
        void asignarResponsable_conUsuarioInactivo_lanzaBadRequestException() {
            testUsuario.setActivo(false);
            AsignarResponsableRequest request = new AsignarResponsableRequest(1L, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(usuarioService.findById(1L)).thenReturn(testUsuario);

            assertThatThrownBy(() -> solicitudService.asignarResponsable(1L, request, "admin"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("No se puede asignar un usuario inactivo");
        }

        @Test
        @DisplayName("Falla al asignar responsable a solicitud cerrada")
        void asignarResponsable_aSolicitudCerrada_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.CERRADA);
            AsignarResponsableRequest request = new AsignarResponsableRequest(1L, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            assertThatThrownBy(() -> solicitudService.asignarResponsable(1L, request, "admin"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("No se puede asignar responsable a una solicitud cerrada");
        }
    }

    @Nested
    @DisplayName("Tests de Cerrar Solicitud")
    class CerrarSolicitudTests {

        @Test
        @DisplayName("Cierra solicitud exitosamente")
        void cerrarSolicitud_conEstadoAtendida_retornaSolicitudCerrada() {
            testSolicitud.setEstado(EstadoSolicitud.ATENDIDA);
            CerrarSolicitudRequest request = new CerrarSolicitudRequest(
                    "Solicitud resuelta satisfactoriamente", "Se homologaron 3 materias"
            );

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cerrarSolicitud(1L, request, "gestor");

            assertThat(response).isNotNull();
            assertThat(response.estado()).isEqualTo(EstadoSolicitud.CERRADA);
            assertThat(response.resolucion()).isEqualTo("Solicitud resuelta satisfactoriamente");
            assertThat(response.notasCierre()).isEqualTo("Se homologaron 3 materias");

            verify(historialRepository).save(any(Historial.class));
        }

        @Test
        @DisplayName("Falla al cerrar solicitud no en estado ATENDIDA")
        void cerrarSolicitud_conEstadoNoAtendida_lanzaBadRequestException() {
            testSolicitud.setEstado(EstadoSolicitud.EN_ATENCION);
            CerrarSolicitudRequest request = new CerrarSolicitudRequest("Resolucion valida ok", null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));

            assertThatThrownBy(() -> solicitudService.cerrarSolicitud(1L, request, "gestor"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining("No se puede cerrar la solicitud");
        }
    }

    @Nested
    @DisplayName("Tests de Obtener Historial")
    class ObtenerHistorialTests {

        @Test
        @DisplayName("Obtiene historial de solicitud exitosamente")
        void obtenerHistorial_conSolicitudExistente_retornaListaHistorial() {
            Historial historial = Historial.builder()
                    .id(1L)
                    .solicitud(testSolicitud)
                    .fechaHora(Instant.now())
                    .accion(AccionHistorial.REGISTRO)
                    .usuarioResponsable("admin")
                    .observaciones("Solicitud creada")
                    .build();

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(historialRepository.findBySolicitudIdOrderByFechaHoraAsc(1L)).thenReturn(List.of(historial));

            List<HistorialResponse> response = solicitudService.obtenerHistorial(1L);

            assertThat(response).hasSize(1);
            assertThat(response.get(0).accion()).isEqualTo("REGISTRO");
            assertThat(response.get(0).usuarioResponsable()).isEqualTo("admin");
        }

        @Test
        @DisplayName("Falla al obtener historial de solicitud no existente")
        void obtenerHistorial_conSolicitudNoExistente_lanzaResourceNotFoundException() {
            when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> solicitudService.obtenerHistorial(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Solicitud con ID 999 no encontrada");
        }
    }

    @Nested
    @DisplayName("Tests de Transiciones de Estado Validas")
    class TransicionesEstadoTests {

        @Test
        @DisplayName("Transicion REGISTRADA -> CLASIFICADA es valida")
        void transicion_registradaAClasificada_esValida() {
            testSolicitud.setEstado(EstadoSolicitud.REGISTRADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.CLASIFICADA, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cambiarEstado(1L, request, "gestor");

            assertThat(response.estado()).isEqualTo(EstadoSolicitud.CLASIFICADA);
        }

        @Test
        @DisplayName("Transicion CLASIFICADA -> EN_ATENCION es valida")
        void transicion_clasificadaAEnAtencion_esValida() {
            testSolicitud.setEstado(EstadoSolicitud.CLASIFICADA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.EN_ATENCION, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cambiarEstado(1L, request, "gestor");

            assertThat(response.estado()).isEqualTo(EstadoSolicitud.EN_ATENCION);
        }

        @Test
        @DisplayName("Transicion EN_ATENCION -> ATENDIDA es valida")
        void transicion_enAtencionAAtendida_esValida() {
            testSolicitud.setEstado(EstadoSolicitud.EN_ATENCION);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.ATENDIDA, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cambiarEstado(1L, request, "gestor");

            assertThat(response.estado()).isEqualTo(EstadoSolicitud.ATENDIDA);
        }

        @Test
        @DisplayName("Transicion ATENDIDA -> CERRADA es valida")
        void transicion_atendidaACerrada_esValida() {
            testSolicitud.setEstado(EstadoSolicitud.ATENDIDA);
            CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.CERRADA, null);

            when(solicitudRepository.findById(1L)).thenReturn(Optional.of(testSolicitud));
            when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudResponse response = solicitudService.cambiarEstado(1L, request, "gestor");

            assertThat(response.estado()).isEqualTo(EstadoSolicitud.CERRADA);
        }
    }
}
