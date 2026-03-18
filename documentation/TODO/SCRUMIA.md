# Scrum – Requerimientos Adicionales (IA Opcional)

Basado en los FR opcionales del proyecto:

FR-09 Resumenes generados por IA

FR-10 Sugerencia automatica de clasificacion/prioridad

FR-11 Operacion independiente de IA

Estos requerimientos son opcionales, pero evaluables como valor agregado.

1️⃣ Objetivo

Agregar asistencia con IA sin romper el sistema base.

Condicion obligatoria:

El sistema debe funcionar completamente sin IA (FR-11).

2️⃣ Objetivo del Producto

Implementar IA como un servicio desacoplado que:

- [ ] Sugiera clasificacion/prioridad
- [ ] Genere resumenes
- [ ] Sea opcional y pueda deshabilitarse

3️⃣ Backlog de IA

🔹 Diseno de Arquitectura de IA

- [ ] Definir interfaz AIService
- [ ] Disenar adapter para LLM externo
- [ ] Disenar fallback sin IA
- [ ] Definir timeout y reintentos
- [ ] Definir manejo de errores de IA
- [ ] Definir feature flag de IA ON/OFF

🔹 FR-10 Sugerencia de Clasificacion/Prioridad

- [ ] Endpoint /ai/suggest-classification
- [ ] Enviar descripcion de solicitud al LLM
- [ ] Recibir tipo sugerido
- [ ] Recibir prioridad sugerida
- [ ] Mostrar sugerencia en UI
- [ ] Permitir confirmacion humana
- [ ] Registrar historial de sugerencias

🔹 FR-09 Resumen de Solicitud

- [ ] Endpoint /ai/summary
- [ ] Enviar historial de solicitud al LLM
- [ ] Generar resumen en texto
- [ ] Mostrar resumen en UI
- [ ] Guardar resumen opcionalmente
- [ ] Validar longitud y formato

🔹 FR-11 Independencia de IA

- [ ] Sistema funcional sin API de IA
- [ ] Manejo graceful de fallback
- [ ] Mensaje "IA no disponible"
- [ ] Pruebas sin IA
- [ ] Pruebas con IA mock

🔹 Seguridad y Costos

- [ ] Sanitizar entradas
- [ ] Limitar tokens
- [ ] Controlar rate limit
- [ ] Ocultar datos sensibles
- [ ] Logging de uso de IA

🔹 UX de IA

- [ ] Etiquetar sugerencias como "IA"
- [ ] Permitir editar sugerencias
- [ ] Mostrar confianza de IA
- [ ] Boton para regenerar resumen

🔹 Pruebas de IA

- [ ] Pruebas con IA mock
- [ ] Pruebas de fallback
- [ ] Pruebas de seguridad de prompts
- [ ] Pruebas de rendimiento

4️⃣ Planificacion de Sprint IA (2 semanas sugeridas)

Semana 1 – Backend IA

- [ ] Disenar interfaz de IA
- [ ] Implementar adapter LLM
- [ ] Endpoint de sugerencias
- [ ] Endpoint de resumen
- [ ] Pruebas mock

Semana 2 – Frontend + Validacion

- [ ] UI de sugerencia de clasificacion
- [ ] UI de resumen de historial
- [ ] Confirmacion humana
- [ ] Fallback sin IA
- [ ] Demo de IA

5️⃣ Definicion de Hecho para IA

- [ ] IA desacoplada
- [ ] Sistema funcional sin IA
- [ ] Sugerencias editables
- [ ] Resumen funcional
- [ ] Pruebas pasan
- [ ] Costos controlados

6️⃣ Riesgos Tecnicos de IA

- [ ] Dependencia critica de API externa
- [ ] Envio de datos sensibles
- [ ] Costos inesperados
- [ ] Alta latencia
- [ ] Respuestas incorrectas de IA

7️⃣ Metricas de IA

- [ ] % de sugerencias aceptadas por usuarios
- [ ] Tiempo de respuesta de IA
- [ ] No. de errores de IA
- [ ] Costo por solicitud
- [ ] Uso total de IA
