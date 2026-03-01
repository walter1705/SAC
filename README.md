# SAC – Academic Request Triage System

SAC (Sistema de Atención y Clasificación) is a web-based Academic Request Triage and Management System designed to centralize, classify, prioritize, and track academic and administrative requests within a university program.

The system is built using:

- Backend: Spring Boot (Java)
- Frontend: Next.js (React)
- Database: PostgreSQL (recommended)
- Authentication: JWT-based security
- Optional Value-Added Feature: AI-assisted classification and summarization

---

# 1. Project Overview

## Problem

Academic requests (course registration, homologations, cancellations, seat requests, etc.) are often managed through multiple channels (email, in-person, phone), leading to:

- Lack of traceability
- Delayed responses
- Operational overload
- No structured prioritization

## Solution

SAC provides:

- Structured request registration
- Rule-based prioritization
- Controlled assignment of responsible staff
- Full lifecycle state management
- Auditable request history
- REST API architecture
- Optional AI assistance (non-critical)

---

# 2. Architecture

SAC follows a layered architecture:

Backend (Spring Boot):

- Controller Layer (REST API)
- Service Layer (Business Logic)
- Domain Layer (Core Entities & Rules)
- Repository Layer (JPA/Hibernate)
- Security Layer (JWT + Role-based Authorization)

Frontend (Next.js):

- App Router or Pages Router
- Modular component architecture
- API service layer
- Auth context (JWT management)
- Role-based UI rendering

System communication:

Frontend → REST API → Database

AI integration (optional):

Backend → LLM Service → External Provider

Important: The system must operate fully without AI.

---

# 3. Core Features

## Request Management

- Register academic requests
- Classify by request type
- Assign priority using business rules
- Manage lifecycle states:
  - Registered
  - Classified
  - In Progress
  - Resolved
  - Closed
- Assign responsible staff
- Maintain immutable audit history
- Filter and search requests
- Restrict operations by role

## Optional AI Features

- Suggest request type and priority from description
- Generate request summary from history
- Fully optional and failsafe

---

# 4. Technology Stack

Backend:

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway (recommended)
- Swagger/OpenAPI

Frontend:

- Next.js
- React
- TypeScript
- Axios or Fetch API
- JWT authentication
- Tailwind CSS or CSS Modules

Deployment:

- Docker (optional but recommended)
- Nginx (optional)
- Cloud VM or local server

---

# 5. Domain Model (Core Entities)

- Request
- User
- Responsible
- RequestHistory
- RequestState (enum)
- Priority (enum)
- RequestType (enum)

Key business rules:

- Closed requests cannot be modified
- State transitions must be valid
- Priority must include justification
- History must be immutable
- Responsible must be active

---

# 6. Security Model

Roles:

- STUDENT
- STAFF
- COORDINATOR

Authorization rules:

- Students can create requests
- Staff can classify and update
- Coordinators can close
- Only authorized roles can assign responsible

Authentication:

- JWT-based
- Token required for protected endpoints
- Role validation at endpoint level

---

# 7. API Structure (Example Endpoints)

POST /api/requests  
GET /api/requests  
GET /api/requests/{id}  
PUT /api/requests/{id}/classify  
PUT /api/requests/{id}/priority  
PUT /api/requests/{id}/assign  
PUT /api/requests/{id}/state  
PUT /api/requests/{id}/close  
GET /api/requests/{id}/history

Optional:

POST /api/ai/suggest  
POST /api/ai/summary

---

# 8. Installation

## Backend Setup

1. Clone repository
2. Configure application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/sac
spring.datasource.username=your_user
spring.datasource.password=your_password
jwt.secret=your_secret_key

3. Run:

./mvnw spring-boot:run

Backend default port:
http://localhost:8080

---

## Frontend Setup

1. Navigate to frontend directory
2. Install dependencies:

npm install

3. Configure environment file:

NEXT_PUBLIC_API_URL=http://localhost:8080/api

4. Run:

npm run dev

Frontend default port:
http://localhost:3000

---

# 9. Environment Variables

Backend:

- DB_URL
- DB_USERNAME
- DB_PASSWORD
- JWT_SECRET
- AI_API_KEY (optional)

Frontend:

- NEXT_PUBLIC_API_URL

---

# 10. Development Workflow

Project divided into 3 milestones:

Milestone 1 – Design & Modeling  
Milestone 2 – Backend & Business Logic  
Milestone 3 – Frontend, Security & Deployment

Scrum-based incremental development.

---

# 11. Testing

Backend:

- Unit tests (Service layer)
- Integration tests (API endpoints)
- State machine tests

Frontend:

- Component tests
- Integration tests
- Role-based UI tests

---

# 12. AI Integration (Optional)

AI service must:

- Be isolated behind an interface
- Have fallback logic
- Never break core functionality
- Allow human confirmation of suggestions
- Be safely disableable

AI failure must not affect system stability.

---

# 13. Deployment

Recommended:

- Docker Compose (backend + frontend + database)
- Production build:
  - mvn clean package
  - npm run build
- Configure environment variables securely
- Use HTTPS in production

---

# 14. Definition of Done

The system is considered complete when:

- All core requirements function correctly
- Security roles are enforced
- State transitions are validated
- History is auditable
- System works without AI
- No critical runtime errors
- Application is deployable

---

# 15. Future Improvements

- Email notifications
- Advanced reporting dashboard
- SLA tracking
- Multi-program support
- Configurable priority engine
- Analytics dashboard
- Rate limiting
- Observability (Prometheus + Grafana)

---

# 16. Author

Project: SAC – Academic Request Triage System  
Technology: Spring Boot + Next.js  
Architecture: Layered REST-based

---

# 17. License

Educational project.
