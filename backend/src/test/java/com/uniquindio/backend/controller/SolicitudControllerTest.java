package com.uniquindio.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.backend.model.dto.request.AsignarResponsableRequest;
import com.uniquindio.backend.model.dto.request.CambiarEstadoRequest;
import com.uniquindio.backend.model.dto.request.CerrarSolicitudRequest;
import com.uniquindio.backend.model.dto.request.ClasificarSolicitudRequest;
import com.uniquindio.backend.model.dto.request.CrearSolicitudRequest;
import com.uniquindio.backend.model.dto.response.AsignacionResponse;
import com.uniquindio.backend.model.dto.response.HistorialResponse;
import com.uniquindio.backend.model.dto.response.SolicitudResponse;
import com.uniquindio.backend.model.dto.response.SolicitudesPaginadasResponse;
import com.uniquindio.backend.model.enums.CanalOrigen;
import com.uniquindio.backend.model.enums.EstadoSolicitud;
import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;
import com.uniquindio.backend.service.SolicitudService;
import com.uniquindio.backend.util.config.JacksonConfig;
import com.uniquindio.backend.util.exception.BadRequestException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import com.uniquindio.backend.util.security.JwtAuthenticationEntryPoint;
import com.uniquindio.backend.util.security.JwtAuthenticationFilter;
import com.uniquindio.backend.util.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import({SolicitudControllerTest.TestSecurityConfig.class, JacksonConfig.class})
@EnableAutoConfiguration
class SolicitudControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudService solicitudService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private SolicitudResponse testSolicitudResponse;

    @BeforeEach
    void setUp() {
        testSolicitudResponse = new SolicitudResponse(
                1L,
                "Pedro Estudiante",
                "pedro@uni.edu.co",
                "3001234567",
                "12345678",
                "Solicitud de homologacion",
                "Necesito homologar materias",
                CanalOrigen.WEB,
                Instant.now(),
                null,
                null,
                null,
                EstadoSolicitud.REGISTRADA,
                null,
                null
        );
    }

    @Test
    @DisplayName("Crear solicitud exitosa retorna 201 CREATED")
    void crearSolicitud_conDatosValidos_retorna201() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Pedro Estudiante",
                "pedro@uni.edu.co",
                "3001234567",
                "12345678",
                "Solicitud de homologacion",
                "Necesito homologar materias del programa anterior",
                CanalOrigen.WEB
        );

        when(solicitudService.crearSolicitud(any(CrearSolicitudRequest.class), anyString()))
                .thenReturn(testSolicitudResponse);

        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteNombre").value("Pedro Estudiante"))
                .andExpect(jsonPath("$.estado").value("REGISTRADA"));
    }

    @Test
    @DisplayName("Crear solicitud sin asunto retorna 400")
    void crearSolicitud_sinAsunto_retorna400() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Pedro Estudiante",
                "pedro@uni.edu.co",
                "3001234567",
                "12345678",
                null,
                "Descripcion de la solicitud",
                CanalOrigen.WEB
        );

        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear solicitud sin descripcion retorna 400")
    void crearSolicitud_sinDescripcion_retorna400() throws Exception {
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "Pedro Estudiante",
                "pedro@uni.edu.co",
                "3001234567",
                "12345678",
                "Asunto de prueba",
                null,
                CanalOrigen.WEB
        );

        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Listar solicitudes retorna 200 OK")
    void listarSolicitudes_sinFiltros_retorna200() throws Exception {
        SolicitudesPaginadasResponse response = new SolicitudesPaginadasResponse(
                List.of(testSolicitudResponse), 0, 20, 1L, 1
        );

        when(solicitudService.listarSolicitudes(isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    @DisplayName("Listar solicitudes con filtro de estado retorna 200 OK")
    void listarSolicitudes_conFiltroEstado_retorna200() throws Exception {
        SolicitudesPaginadasResponse response = new SolicitudesPaginadasResponse(
                List.of(), 0, 20, 0L, 0
        );

        when(solicitudService.listarSolicitudes(
                eq(EstadoSolicitud.REGISTRADA), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/solicitudes")
                        .param("estado", "REGISTRADA"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Listar solicitudes con paginacion personalizada")
    void listarSolicitudes_conPaginacion_retorna200() throws Exception {
        SolicitudesPaginadasResponse response = new SolicitudesPaginadasResponse(
                List.of(), 1, 10, 15L, 2
        );

        when(solicitudService.listarSolicitudes(isNull(), isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/solicitudes")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagina").value(1))
                .andExpect(jsonPath("$['tamaño']").value(10));
    }

    @Test
    @DisplayName("Obtener solicitud existente retorna 200 OK")
    void obtenerSolicitud_conIdValido_retorna200() throws Exception {
        when(solicitudService.obtenerPorId(1L)).thenReturn(testSolicitudResponse);

        mockMvc.perform(get("/api/v1/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteNombre").value("Pedro Estudiante"));
    }

    @Test
    @DisplayName("Obtener solicitud no existente retorna 404")
    void obtenerSolicitud_noExistente_retorna404() throws Exception {
        when(solicitudService.obtenerPorId(999L))
                .thenThrow(new ResourceNotFoundException("Solicitud con ID 999 no encontrada"));

        mockMvc.perform(get("/api/v1/solicitudes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Clasificar solicitud exitosa retorna 200 OK")
    void clasificarSolicitud_conDatosValidos_retorna200() throws Exception {
        ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(
                TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, "Solicitud prioritaria"
        );

        SolicitudResponse response = new SolicitudResponse(
                1L, null, null, null, null, null, null, null, null,
                TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, null,
                EstadoSolicitud.CLASIFICADA, null, null
        );

        when(solicitudService.clasificarSolicitud(eq(1L), any(ClasificarSolicitudRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/v1/solicitudes/1/clasificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CLASIFICADA"))
                .andExpect(jsonPath("$.tipo").value("HOMOLOGACION"))
                .andExpect(jsonPath("$.prioridad").value("ALTA"));
    }

    @Test
    @DisplayName("Clasificar solicitud ya clasificada retorna 400")
    void clasificarSolicitud_yaClasificada_retorna400() throws Exception {
        ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(
                TipoSolicitud.HOMOLOGACION, Prioridad.ALTA, "Nota"
        );

        when(solicitudService.clasificarSolicitud(eq(1L), any(ClasificarSolicitudRequest.class), anyString()))
                .thenThrow(new BadRequestException("Solo se pueden clasificar solicitudes en estado REGISTRADA"));

        mockMvc.perform(patch("/api/v1/solicitudes/1/clasificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Clasificar solicitud sin tipo retorna 400")
    void clasificarSolicitud_sinTipo_retorna400() throws Exception {
        ClasificarSolicitudRequest request = new ClasificarSolicitudRequest(null, Prioridad.ALTA, "Nota");

        mockMvc.perform(patch("/api/v1/solicitudes/1/clasificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cambiar estado exitoso retorna 200 OK")
    void cambiarEstado_transicionValida_retorna200() throws Exception {
        CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.EN_ATENCION, "Iniciando atencion");

        SolicitudResponse response = new SolicitudResponse(
                1L, null, null, null, null, null, null, null, null,
                null, null, null, EstadoSolicitud.EN_ATENCION, null, null
        );

        when(solicitudService.cambiarEstado(eq(1L), any(CambiarEstadoRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/v1/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_ATENCION"));
    }

    @Test
    @DisplayName("Cambiar estado con transicion invalida retorna 400")
    void cambiarEstado_transicionInvalida_retorna400() throws Exception {
        CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoSolicitud.CERRADA, null);

        when(solicitudService.cambiarEstado(eq(1L), any(CambiarEstadoRequest.class), anyString()))
                .thenThrow(new BadRequestException("Transición de estado inválida"));

        mockMvc.perform(patch("/api/v1/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cambiar estado sin nuevo estado retorna 400")
    void cambiarEstado_sinNuevoEstado_retorna400() throws Exception {
        CambiarEstadoRequest request = new CambiarEstadoRequest(null, "Nota sin estado");

        mockMvc.perform(patch("/api/v1/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Asignar responsable exitoso retorna 201 CREATED")
    void asignarResponsable_conDatosValidos_retorna201() throws Exception {
        AsignarResponsableRequest request = new AsignarResponsableRequest(1L, "Asignacion urgente");

        AsignacionResponse response = new AsignacionResponse(1L, 1L, 1L, Instant.now(), true);

        when(solicitudService.asignarResponsable(eq(1L), any(AsignarResponsableRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/solicitudes/1/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.solicitudId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.activa").value(true));
    }

    @Test
    @DisplayName("Asignar usuario inactivo retorna 400")
    void asignarResponsable_usuarioInactivo_retorna400() throws Exception {
        AsignarResponsableRequest request = new AsignarResponsableRequest(1L, null);

        when(solicitudService.asignarResponsable(eq(1L), any(AsignarResponsableRequest.class), anyString()))
                .thenThrow(new BadRequestException("No se puede asignar un usuario inactivo"));

        mockMvc.perform(post("/api/v1/solicitudes/1/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Asignar sin responsable ID retorna 400")
    void asignarResponsable_sinResponsableId_retorna400() throws Exception {
        AsignarResponsableRequest request = new AsignarResponsableRequest(null, "Nota sin responsable");

        mockMvc.perform(post("/api/v1/solicitudes/1/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cerrar solicitud exitosa retorna 200 OK")
    void cerrarSolicitud_conDatosValidos_retorna200() throws Exception {
        CerrarSolicitudRequest request = new CerrarSolicitudRequest(
                "Solicitud resuelta satisfactoriamente", "Se homologaron 3 materias"
        );

        SolicitudResponse response = new SolicitudResponse(
                1L, null, null, null, null, null, null, null, null, null, null, null,
                EstadoSolicitud.CERRADA,
                "Solicitud resuelta satisfactoriamente",
                "Se homologaron 3 materias"
        );

        when(solicitudService.cerrarSolicitud(eq(1L), any(CerrarSolicitudRequest.class), anyString()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/v1/solicitudes/1/cerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADA"))
                .andExpect(jsonPath("$.resolucion").value("Solicitud resuelta satisfactoriamente"));
    }

    @Test
    @DisplayName("Cerrar solicitud no atendida retorna 400")
    void cerrarSolicitud_noAtendida_retorna400() throws Exception {
        CerrarSolicitudRequest request = new CerrarSolicitudRequest("Resolucion valida ok", null);

        when(solicitudService.cerrarSolicitud(eq(1L), any(CerrarSolicitudRequest.class), anyString()))
                .thenThrow(new BadRequestException("No se puede cerrar la solicitud"));

        mockMvc.perform(patch("/api/v1/solicitudes/1/cerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Cerrar solicitud sin resolucion retorna 400")
    void cerrarSolicitud_sinResolucion_retorna400() throws Exception {
        CerrarSolicitudRequest request = new CerrarSolicitudRequest(null, "Notas sin resolucion");

        mockMvc.perform(patch("/api/v1/solicitudes/1/cerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Obtener historial exitoso retorna 200 OK")
    void obtenerHistorial_conSolicitudExistente_retorna200() throws Exception {
        HistorialResponse historial = new HistorialResponse(
                1L, Instant.now(), "REGISTRO", "admin", "Solicitud creada"
        );

        when(solicitudService.obtenerHistorial(1L)).thenReturn(List.of(historial));

        mockMvc.perform(get("/api/v1/solicitudes/1/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accion").value("REGISTRO"));
    }

    @Test
    @DisplayName("Obtener historial de solicitud no existente retorna 404")
    void obtenerHistorial_solicitudNoExistente_retorna404() throws Exception {
        when(solicitudService.obtenerHistorial(999L))
                .thenThrow(new ResourceNotFoundException("Solicitud con ID 999 no encontrada"));

        mockMvc.perform(get("/api/v1/solicitudes/999/historial"))
                .andExpect(status().isNotFound());
    }
}
