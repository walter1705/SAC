# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SAC (Sistema de Atención y Clasificación) is an academic request triage system for a university program. It centralizes, classifies, prioritizes, and tracks academic/administrative requests through a full lifecycle.

**Stack**: Spring Boot 4 (Java 21) + Angular 21 + PostgreSQL + RabbitMQ

---

## Backend (`/backend`)

### Commands

```bash
# Run (from /backend)
./gradlew bootRun

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.uniquindio.backend.service.SolicitudServiceTest"

# Run tests with output
./gradlew test --info
```

### Infrastructure (Docker)

The backend uses `spring-boot-docker-compose` — running `bootRun` will auto-start the containers defined in `compose.yaml` (PostgreSQL on 5432, RabbitMQ on 5672).

```bash
# Or start manually
docker compose up -d   # from /backend
```

### Architecture

Flat layered package structure under `com.uniquindio.backend`:

```
controller/     — REST endpoints (3 controllers: Solicitud, Usuario, IA)
model/          — JPA entities (Solicitud, Usuario, Asignacion, Historial)
  enums/        — EstadoSolicitud, Prioridad, TipoSolicitud, RolUsuario, CanalOrigen
  dto/request/  — Input DTOs
  dto/response/ — Output DTOs
repository/     — Spring Data JPA repositories
service/        — Business logic (SolicitudService, UsuarioService, IAService)
util/
  config/       — SecurityConfig, JacksonConfig, DataSeeder
  exception/    — GlobalExceptionHandler + custom exceptions
  security/     — JWT filter, utility, entry point
  serviceAI/    — GeminiClient (optional AI integration)
```

### Key Architectural Points

- **State machine** in `SolicitudService` — transitions defined as `Map<EstadoSolicitud, Set<EstadoSolicitud>>`. Valid flow: `REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA`. Closed requests cannot be modified.
- **Immutable audit history** — every operation writes to `Historial` via `registrarHistorial()`. Never modify history entries.
- **Roles**: `CONSULTOR` (creates requests), `PERSONAL` (classifies/updates), `COORDINADOR` (closes, assigns responsible). Enforced at controller level via Spring Security.
- **AI is optional and isolated** — `IAService` wraps `GeminiClient`. Any failure must be caught and returned as `IaNoDisponibleResponse`. Never let AI failure propagate to core flows.
- **JWT**: stateless, secret in `application.properties`, filter in `JwtAuthenticationFilter`.
- Tests use **H2 in-memory** for persistence, `@SpringBootTest` + `MockMvc` for controllers.

### OpenAPI / Swagger

Available at `http://localhost:8080/swagger-ui.html` when running.

---

## Frontend (`/frontend`)

### Commands

```bash
# Install
npm install

# Dev server
npm start          # http://localhost:4200

# Build
npm run build

# Test
npm test
```

### Architecture

Angular 21 app with standalone components, Angular Router, Reactive Forms, and `HttpClient`. The current frontend includes a functional login flow and a protected dashboard route. API base URL is configured in `src/app/core/api.config.ts`.

---

## Domain Language (Español)

The entire codebase uses Spanish naming — entities, fields, methods, DTOs. Keep this consistent:

| Concept | Code name |
|---|---|
| Request | `Solicitud` |
| User/Student | `Usuario` / `Consultor` |
| Assignment | `Asignacion` |
| History | `Historial` |
| State | `EstadoSolicitud` |
| Priority | `Prioridad` |
| Request type | `TipoSolicitud` |
| Origin channel | `CanalOrigen` |
