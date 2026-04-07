package com.uniquindio.backend.util.serviceAI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.backend.util.exception.IAServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GeminiClient {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final double temperature;

    public GeminiClient(
            @Value("${google.ai.api-key}") String apiKey,
            @Value("${google.ai.model}") String model,
            @Value("${google.ai.temperature:0.7}") double temperature,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.model = model;
        this.temperature = temperature;
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }

    public String generarContenido(String prompt) {
        try {
            String url = String.format(GEMINI_API_URL, model) + "?key=" + apiKey;

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                ),
                "generationConfig", Map.of(
                    "temperature", temperature,
                    "maxOutputTokens", 1024
                )
            );

            String jsonRequest = objectMapper.writeValueAsString(requestBody);

            String response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonRequest)
                .retrieve()
                .body(String.class);

            return extraerTextoRespuesta(response);

        } catch (Exception e) {
            log.error("Error al comunicarse con la API de Gemini: {}", e.getMessage());
            throw new IAServiceUnavailableException(
                "El servicio de IA no está disponible en este momento. Opere de forma manual.", e);
        }
    }

    private String extraerTextoRespuesta(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText();
                }
            }
            throw new IAServiceUnavailableException(
                "No se pudo obtener respuesta del modelo de IA.");
        } catch (IAServiceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al parsear respuesta de Gemini: {}", e.getMessage());
            throw new IAServiceUnavailableException(
                "Error al procesar la respuesta del servicio de IA.", e);
        }
    }
}
