package com.uniquindio.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.backend.model.Historial;
import com.uniquindio.backend.model.Solicitud;
import com.uniquindio.backend.model.dto.request.SugerirClasificacionRequest;
import com.uniquindio.backend.model.dto.response.ResumenSolicitudResponse;
import com.uniquindio.backend.model.dto.response.SugerirClasificacionResponse;
import com.uniquindio.backend.model.enums.Prioridad;
import com.uniquindio.backend.model.enums.TipoSolicitud;
import com.uniquindio.backend.repository.HistorialRepository;
import com.uniquindio.backend.repository.SolicitudRepository;
import com.uniquindio.backend.util.exception.IAServiceUnavailableException;
import com.uniquindio.backend.util.exception.ResourceNotFoundException;
import com.uniquindio.backend.util.serviceAI.GeminiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IAService {

    private final GeminiClient geminiClient;
    private final SolicitudRepository solicitudRepository;
    private final HistorialRepository historialRepository;
    private final ObjectMapper objectMapper;

    private static final String PROMPT_CLASIFICACION = """
        Eres un asistente de clasificación de solicitudes académicas universitarias.

        Analiza el siguiente texto de una solicitud académica y determina:
        1. El tipo de solicitud (debe ser EXACTAMENTE uno de: REGISTRO, HOMOLOGACION, CANCELACION, CUPOS, CONSULTA)
        2. La prioridad (debe ser EXACTAMENTE una de: ALTA, MEDIA, BAJA)
        3. Una justificación breve del por qué
        4. Un nivel de confianza entre 0.0 y 1.0

        Reglas de prioridad:
        - CANCELACION cerca de fecha límite = ALTA
        - HOMOLOGACION = ALTA
        - REGISTRO durante período de matrícula = ALTA
        - CUPOS sin cupos disponibles = ALTA
        - CONSULTA = BAJA
        - Por defecto = MEDIA

        Responde ÚNICAMENTE con un JSON válido en este formato exacto (sin markdown, sin texto adicional):
        {"tipoSugerido":"TIPO","prioridadSugerida":"PRIORIDAD","justificacion":"texto","confianza":0.85}

        Texto de la solicitud a analizar:
        %s
        """;

    private static final String PROMPT_RESUMEN = """
        Eres un asistente que genera resúmenes concisos de solicitudes académicas.

        Genera un resumen narrativo breve (máximo 3 oraciones) del estado actual e historial
        de la siguiente solicitud académica. El resumen debe ser útil para que un gestor
        entienda rápidamente el caso.

        Información de la solicitud:
        - ID: %d
        - Estudiante: %s (ID: %s)
        - Asunto: %s
        - Descripción: %s
        - Canal de origen: %s
        - Fecha de registro: %s
        - Estado actual: %s
        - Tipo: %s
        - Prioridad: %s

        Historial de acciones:
        %s

        Genera un resumen breve y profesional en español. Responde SOLO con el texto del resumen,
        sin formato adicional.
        """;

    public SugerirClasificacionResponse sugerirClasificacion(SugerirClasificacionRequest request) {
        try {
            String prompt = String.format(PROMPT_CLASIFICACION, request.descripcion());
            String respuesta = geminiClient.generarContenido(prompt);
            return parsearRespuestaClasificacion(respuesta);
        } catch (IAServiceUnavailableException e) {
            log.warn("IA no disponible para sugerencia de clasificación: {}", e.getMessage());
            return new SugerirClasificacionResponse(
                null,
                null,
                "Servicio de IA no disponible para este servicio. Clasifique la solicitud manualmente.",
                0.0f
            );
        }
    }

    @Transactional(readOnly = true)
    public ResumenSolicitudResponse generarResumen(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new ResourceNotFoundException("Solicitud con ID " + solicitudId + " no encontrada"));

        List<Historial> historial = historialRepository.findBySolicitudIdOrderByFechaHoraAsc(solicitudId);

        String historialTexto = formatearHistorial(historial);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

        String prompt = String.format(PROMPT_RESUMEN,
            solicitud.getId(),
            solicitud.getSolicitanteNombre(),
            solicitud.getSolicitanteIdentificacion(),
            solicitud.getAsunto(),
            solicitud.getDescripcion(),
            solicitud.getCanalOrigen(),
            formatter.format(solicitud.getFechaHoraRegistro()),
            solicitud.getEstado(),
            solicitud.getTipo() != null ? solicitud.getTipo() : "Sin clasificar",
            solicitud.getPrioridad() != null ? solicitud.getPrioridad() : "Sin asignar",
            historialTexto
        );

        try {
            String resumen = geminiClient.generarContenido(prompt);
            return new ResumenSolicitudResponse(solicitudId, resumen.trim(), Instant.now());
        } catch (IAServiceUnavailableException e) {
            log.warn("IA no disponible para resumen de solicitud {}: {}", solicitudId, e.getMessage());
            return new ResumenSolicitudResponse(
                solicitudId,
                generarResumenManual(solicitud, historial, formatter),
                Instant.now()
            );
        }
    }

    private String generarResumenManual(Solicitud solicitud, List<Historial> historial, DateTimeFormatter formatter) {
        String tipo = solicitud.getTipo() != null ? solicitud.getTipo().name() : "Sin clasificar";
        String prioridad = solicitud.getPrioridad() != null ? solicitud.getPrioridad().name() : "Sin asignar";
        String fechaRegistro = formatter.format(solicitud.getFechaHoraRegistro());
        int totalAcciones = historial.size();

        return String.format(
            "[Servicio de IA no disponible — resumen generado sin inteligencia artificial]\n\n" +
            "Solicitud #%d registrada el %s por %s (ID: %s) a través del canal %s.\n" +
            "Asunto: \"%s\".\n" +
            "Estado actual: %s | Tipo: %s | Prioridad: %s.\n" +
            "%s",
            solicitud.getId(),
            fechaRegistro,
            solicitud.getSolicitanteNombre(),
            solicitud.getSolicitanteIdentificacion(),
            solicitud.getCanalOrigen(),
            solicitud.getAsunto(),
            solicitud.getEstado(),
            tipo,
            prioridad,
            totalAcciones == 0
                ? "Sin acciones registradas en el historial."
                : totalAcciones + " acción(es) registrada(s) en el historial."
        );
    }

    private String formatearHistorial(List<Historial> historial) {
        if (historial.isEmpty()) {
            return "Sin acciones registradas";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

        StringBuilder sb = new StringBuilder();
        for (Historial h : historial) {
            sb.append(String.format("- %s: %s por %s - %s%n",
                formatter.format(h.getFechaHora()),
                h.getAccion(),
                h.getUsuarioResponsable(),
                h.getObservaciones() != null ? h.getObservaciones() : ""
            ));
        }
        return sb.toString();
    }

    private SugerirClasificacionResponse parsearRespuestaClasificacion(String respuesta) {
        try {
            String jsonLimpio = limpiarJson(respuesta);
            JsonNode json = objectMapper.readTree(jsonLimpio);

            String tipoStr = json.path("tipoSugerido").asText();
            String prioridadStr = json.path("prioridadSugerida").asText();
            String justificacion = json.path("justificacion").asText();
            float confianza = (float) json.path("confianza").asDouble(0.5);

            TipoSolicitud tipo = TipoSolicitud.valueOf(tipoStr.toUpperCase());
            Prioridad prioridad = Prioridad.valueOf(prioridadStr.toUpperCase());

            return new SugerirClasificacionResponse(tipo, prioridad, justificacion, confianza);

        } catch (Exception e) {
            log.error("Error al parsear respuesta de clasificación: {}", e.getMessage());
            throw new IAServiceUnavailableException(
                "No se pudo interpretar la respuesta del modelo de IA. Clasifique la solicitud manualmente.");
        }
    }

    private String limpiarJson(String respuesta) {
        String limpio = respuesta.trim();
        if (limpio.startsWith("```json")) {
            limpio = limpio.substring(7);
        } else if (limpio.startsWith("```")) {
            limpio = limpio.substring(3);
        }
        if (limpio.endsWith("```")) {
            limpio = limpio.substring(0, limpio.length() - 3);
        }
        return limpio.trim();
    }
}
