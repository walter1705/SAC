# HITO 2 – Scrum Detallado

Fase: Backend y Logica (Semanas 6–10)
Stack: Spring Boot + ORM + REST API
Enfoque: Implementar todos los FR funcionales del backend.

Basado en la guia del Sistema de Triaje Academico y Gestion de Solicitudes.

1️⃣ Objetivo del Hito

Construir un backend funcional que:

- [ ] Implemente FR-01 → FR-08
- [ ] Exponga REST API (FR-12)
- [ ] Incluya autorizacion basica (FR-13)
- [ ] Sea funcional sin IA (FR-11)

2️⃣ Objetivo del Producto del Hito

Backend listo para consumo desde Angular sin refactorizaciones mayores.

3️⃣ Backlog del Producto del Hito 2
🔹 Configuracion Base

- [ ] Crear proyecto Spring Boot
- [ ] Configurar base de datos (PostgreSQL/MySQL)
- [ ] Configurar JPA/Hibernate
- [ ] Configurar migraciones (Flyway o Liquibase)
- [ ] Configurar logging
- [ ] Configurar manejo global de excepciones

🔹 Implementacion del Dominio

- [ ] Entidad Solicitud
- [ ] Entidad Usuario
- [ ] Entidad HistorialSolicitud
- [ ] Enum EstadoSolicitud
- [ ] Enum Prioridad
- [ ] Relaciones ORM correctas
- [ ] Restricciones de BD

🔹 Implementacion de FR

- [ ] FR-01 Registro de solicitudes
  - [ ] Crear endpoint de registro
  - [ ] Validaciones de campos obligatorios
  - [ ] Persistencia correcta
  - [ ] Auditoria inicial
- [ ] FR-02 Clasificacion
  - [ ] Endpoint para clasificar solicitud
  - [ ] Validar tipo permitido
  - [ ] Registrar historial
- [ ] FR-03 Priorizacion
  - [ ] Implementar motor de reglas
  - [ ] Calcular prioridad automatica
  - [ ] Guardar justificacion
  - [ ] Permitir ajuste manual
- [ ] FR-04 Ciclo de vida
  - [ ] Implementar maquina de estados
  - [ ] Validar transiciones
  - [ ] Registrar historial por cambio
  - [ ] Evitar estados invalidos
- [ ] FR-05 Asignacion de responsables
  - [ ] Endpoint para asignar responsable
  - [ ] Validar responsable activo
  - [ ] Registrar historial
- [ ] FR-06 Historial auditable
  - [ ] Tabla de historial
  - [ ] Registrar accion
  - [ ] Registrar usuario
  - [ ] Registrar fecha
  - [ ] Registrar observaciones
- [ ] FR-07 Consultas de solicitudes
  - [ ] Endpoint con filtro por estado
  - [ ] Filtrar por prioridad
  - [ ] Filtrar por tipo
  - [ ] Filtrar por responsable
  - [ ] Paginacion
- [ ] FR-08 Cierre de solicitudes
  - [ ] Validar estado previo
  - [ ] Exigir observacion
  - [ ] Bloquear edicion posterior

🔹 REST API (FR-12)

- [ ] Controladores REST
- [ ] DTOs request/response
- [ ] Bean Validation
- [ ] Codigos HTTP correctos
- [ ] Manejo estandar de errores
- [ ] Documentacion Swagger/OpenAPI

🔹 Seguridad Basica (FR-13)

- [ ] Autenticacion JWT basica o mock
- [ ] Roles definidos
- [ ] Restricciones de endpoints
- [ ] Auditoria de usuario

🔹 IA Opcional

- [ ] Implementar FR-09 o FR-10
- [ ] Crear interfaz de servicio IA
- [ ] Fallback sin IA (FR-11)

🔹 Pruebas

- [ ] Pruebas unitarias de servicios
- [ ] Pruebas de repositorios
- [ ] Pruebas de integracion de API
- [ ] Pruebas de reglas de prioridad
- [ ] Pruebas de maquina de estados

🔹 Calidad de Codigo

- [ ] Code review semanal
- [ ] Checkstyle / Sonar
- [ ] Documentacion JavaDoc
- [ ] README tecnico del backend

4️⃣ Planificacion Interna de Sprint
Semana 6 – Configuracion Base

- [ ] Crear proyecto
- [ ] BD + ORM
- [ ] Entidades principales
- [ ] Migraciones

Semana 7 – FR-01 a FR-03

- [ ] Registro de solicitudes
- [ ] Clasificacion
- [ ] Priorizacion

Semana 8 – FR-04 a FR-06

- [ ] Maquina de estados
- [ ] Asignacion de responsables
- [ ] Historial auditable

Semana 9 – FR-07 a FR-08 + Seguridad

- [ ] Consultas con filtros
- [ ] Cierre de solicitudes
- [ ] Roles basicos
- [ ] Swagger

Semana 10 – Pruebas y Refactorizacion

- [ ] Pruebas de integracion
- [ ] Corregir bugs
- [ ] Documentacion
- [ ] Demo de backend

5️⃣ Definicion de Hecho (Hito 2)

El backend esta listo cuando:

- [ ] Todos los FR de backend funcionan
- [ ] La API responde sin errores criticos
- [ ] Los estados se validan correctamente
- [ ] El historial auditable es completo
- [ ] Las pruebas pasan
- [ ] La seguridad basica funciona
- [ ] Funciona sin IA

6️⃣ Entregables del Hito

- [ ] Repositorio de backend funcional
- [ ] Base de datos persistente
- [ ] Swagger documentado
- [ ] Pruebas basicas
- [ ] Demos de endpoints
