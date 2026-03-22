# HITO 1 – Scrum Detallado

Fase: Diseno y Modelado (Semanas 1–5)
Enfoque: Dominio, arquitectura y contratos de API

1️⃣ Objetivo del Hito

Definir formalmente:

- [x] Modelo de dominio
- [x] Arquitectura del sistema
- [x] Estados del ciclo de vida
- [x] Contratos de REST API
- [x] Estrategia de reglas de negocio

Sin implementar aun codigo de produccion.

2️⃣ Objetivo del Producto del Hito

Tener un diseno validado que permita iniciar la implementacion del backend sin ambiguedades.

3️⃣ Backlog del Producto del Hito 1 (Derivado de FR)
🔹 Analisis de Requisitos

- [x] Descomponer FR-01 a FR-13
- [x] Identificar reglas explicitas e implicitas
- [x] Identificar restricciones tecnicas
- [x] Definir actores del sistema
- [x] Definir permisos por rol (base para FR-13)
- [x] Detectar ambiguedades en el documento

🔹 Modelado del Dominio

- [x] Identificar entidades principales
- [x] Definir atributos obligatorios por entidad
- [x] Definir relaciones (1:N, N:M)
- [x] Modelar entidad Solicitud
- [x] Modelar entidad Usuario
- [x] Modelar entidad Asignado
- [x] Modelar entidad HistorialSolicitud
- [x] Modelar enum EstadoSolicitud
- [x] Modelar enum Prioridad
- [x] Definir invariantes del dominio

🔹 Modelado del Ciclo de Vida (FR-04)

Definir estados:

- [x] Registrada
- [x] Clasificada
- [x] En Progreso
- [x] Resuelta
- [x] Cerrada
- [x] Definir transiciones validas
- [x] Documentar transiciones invalidas
- [x] Definir reglas de negocio por transicion

🔹 Priorizacion (FR-03)

- [x] Definir criterios de impacto academico
- [x] Definir reglas de prioridad
- [x] Documentar logica de decision
- [x] Definir estructura para justificar prioridad

🔹 Diseno de Arquitectura

Definir arquitectura por capas:

- [x] Controller
- [x] Service
- [x] Repository
- [x] Domain
- [x] Definir patron principal
- [x] Definir estrategias de desacoplamiento
- [x] Definir manejo global de excepciones
- [x] Definir estrategia de validacion

🔹 Diseno de REST API (FR-12)

- [x] Definir endpoints CRUD de Solicitud
- [x] Definir endpoint de clasificacion
- [x] Definir endpoint de priorizacion
- [x] Definir endpoint de cambio de estado
- [x] Definir endpoint de asignacion de responsable
- [x] Definir endpoint de historial
- [x] Definir filtros de consulta
- [x] Definir estructura JSON de request/response
- [x] Definir codigos HTTP correctos
- [x] Documentar contrato de API

🔹 Seguridad Base (FR-13)

Definir roles:

- [x] Estudiante
- [x] Personal Administrativo
- [x] Coordinador
- [x] Mapear permisos a endpoints

🔹 IA (Diseno Conceptual)

- [x] Definir que FR de IA se implementara (09 o 10)
- [x] Disenar punto de integracion
- [x] Asegurar independencia (FR-11)
