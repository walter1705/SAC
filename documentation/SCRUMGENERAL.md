# Scrum General – Sistema de Triaje Academico

📌 Proyecto

Sistema de Triaje Academico y Gestion de Solicitudes
Stack: Spring Boot + Angular + REST API + ORM
Metodologia: Scrum incremental en 3 HITOS (5 semanas cada uno)

✅ HITO 1 – Diseno y Modelado (Semanas 1–5)
Objetivo

Definir la arquitectura, el dominio y los contratos del sistema.

Backlog del Hito
🔹 Analisis de Requisitos

- [x] Analizar todos los FR del documento
- [x] Identificar actores del sistema
- [x] Definir casos de uso principales
- [x] Validar alcance del proyecto con el equipo

🔹 Modelado del Dominio

- [x] Crear diagrama de clases UML
- [x] Modelar entidad Solicitud
- [x] Modelar entidad Usuario / Asignado
- [x] Modelar Historial de Solicitud
- [x] Modelar Estados del Ciclo de Vida

🔹 Diseno de Arquitectura

- [x] Definir arquitectura por capas
- [x] Disenar estructura de paquetes de Spring Boot
- [x] Definir entidades ORM
- [x] Definir patrones de diseno a usar
- [x] Definir integracion opcional de IA

🔹 Diseno de REST API

- [x] Definir endpoints para FR-01 a FR-08
- [x] Definir JSON de request/response
- [x] Documentar contratos de API
- [x] Definir codigos de estado HTTP

🔹 Scrum Interno

- [x] Planificacion semanal de sprint
- [x] Daily standups
- [x] Modelar sprint review
- [x] Retrospectiva

✔ Entregables del Hito 1

- [x] UML completo
- [x] Diseno de arquitectura
- [x] Documento de API
- [x] Backlog refinado del Hito 2

✅ HITO 2 – Backend y Logica (Semanas 6–10)
Objetivo

Implementar un backend funcional con Spring Boot.

Backlog del Hito
🔹 Configuracion Base

- [x] Crear proyecto Spring Boot
- [x] Configurar base de datos
- [x] Configurar ORM (JPA/Hibernate)
- [x] Configurar repositorios

🔹 Implementacion de FR

- [ ] FR-01 Registro de solicitudes
- [ ] FR-02 Clasificacion de solicitudes
- [ ] FR-03 Priorizacion basada en reglas
- [ ] FR-04 Gestion de estados
- [ ] FR-05 Asignacion de responsables
- [ ] FR-06 Historial auditable
- [ ] FR-07 Consultas con filtros
- [ ] FR-08 Cierre de solicitudes

🔹 Seguridad Basica

- [ ] Roles de usuario
- [ ] Restricciones de endpoints
- [ ] Validacion de permisos

🔹 REST API

- [ ] Implementar controladores
- [ ] Validar respuestas
- [ ] Manejo de errores
- [ ] Pruebas basicas de API

🔹 IA Opcional

- [ ] Implementar FR-09 o FR-10
- [ ] Garantizar funcionamiento sin IA (FR-11)

🔹 Scrum Interno

- [ ] Demos de sprint del backend
- [ ] Revisiones de codigo
- [ ] Retrospectiva tecnica

✔ Entregables del Hito 2

- [ ] Backend funcional
- [ ] REST API operativa
- [ ] Base de datos persistente
- [ ] Pruebas basicas

✅ HITO 3 – Frontend y Seguridad (Semanas 11–15)
Objetivo

Desarrollar la interfaz Angular e integrar todo el sistema.

Backlog del Hito
🔹 Configuracion de Angular

- [ ] Crear proyecto Angular
- [ ] Configurar rutas
- [ ] Configurar servicios de API

🔹 Interfaces

- [ ] Formulario de registro de solicitudes
- [ ] Panel de clasificacion/priorizacion
- [ ] Vista de historial de solicitudes
- [ ] Panel de responsables
- [ ] Busqueda de solicitudes
- [ ] Vista de estado de solicitudes

🔹 Seguridad

- [ ] Autenticacion JWT
- [ ] Autorizacion por roles
- [ ] Proteccion de rutas

🔹 Integracion Completa

- [ ] Conectar frontend con backend
- [ ] Validar flujos completos
- [ ] Pruebas funcionales
- [ ] Manejo de errores en UI

🔹 Despliegue

- [ ] Preparar entorno de produccion
- [ ] Docker opcional
- [ ] Desplegar en servidor
- [ ] Documentacion tecnica

🔹 Scrum Interno

- [ ] Demo final de sprint
- [ ] Pruebas QA
- [ ] Retrospectiva final

✔ Entregables del Hito 3

- [ ] Sistema totalmente funcional
- [ ] Seguridad implementada
- [ ] Despliegue listo
- [ ] Documentacion final

📊 Lista General de Requisitos Funcionales

- [ ] FR-01 Registro de solicitudes
- [ ] FR-02 Clasificacion
- [ ] FR-03 Priorizacion
- [ ] FR-04 Gestion de estados
- [ ] FR-05 Asignacion de responsables
- [ ] FR-06 Historial auditable
- [ ] FR-07 Consulta
- [ ] FR-08 Cierre de solicitudes
- [ ] FR-09 Resumen con IA (opcional)
- [ ] FR-10 Sugerencia con IA (opcional)
- [ ] FR-11 Funciona sin IA
- [ ] FR-12 REST API
- [ ] FR-13 Autorizacion basica

📅 Scrum Semanal Estandar

- [ ] Sprint Planning
- [ ] Daily Standup
- [ ] Sprint Review
- [ ] Retrospectiva
- [ ] Actualizar backlog
- [ ] Actualizar documentacion

📌 Definicion de Hecho (DoD)

- [ ] Codigo probado
- [ ] API documentada
- [ ] Sin errores criticos
- [ ] Revisado por el equipo
- [ ] Funcionalidad demostrada
- [ ] Integrado al sistema
