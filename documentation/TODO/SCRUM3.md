# MILESTONE 2 – Detailed Scrum

Phase: Backend and Logic (Weeks 6–10)
Stack: Spring Boot + ORM + REST API
Focus: Implement all functional backend FRs.

Based on the Academic Triage and Request Management System guide

.

1️⃣ Milestone Objective

Build a functional backend that:

- [ ] Implements FR-01 → FR-08
- [ ] Exposes REST API (FR-12)
- [ ] Includes basic authorization (FR-13)
- [ ] Is functional without AI (FR-11)

2️⃣ Milestone Product Goal

Backend ready for Angular consumption without major refactoring.

3️⃣ Milestone 2 Product Backlog
🔹 Base Configuration

- [ ] Create Spring Boot project
- [ ] Configure database (PostgreSQL/MySQL)
- [ ] Configure JPA/Hibernate
- [ ] Configure migrations (Flyway or Liquibase)
- [ ] Configure logging
- [ ] Configure global exception handling

🔹 Domain Implementation

- [ ] Request entity
- [ ] User entity
- [ ] RequestHistory entity
- [ ] RequestStatus enum
- [ ] Priority enum
- [ ] Correct ORM relationships
- [ ] DB constraints

🔹 FR Implementation
FR-01 Request registration

- [ ] Create request endpoint
- [ ] Required field validations
- [ ] Correct persistence
- [ ] Initial audit

FR-02 Classification

- [ ] Classify request endpoint
- [ ] Validate allowed type
- [ ] Record history

FR-03 Prioritization

- [ ] Implement rules engine
- [ ] Calculate automatic priority
- [ ] Save justification
- [ ] Allow manual adjustment

FR-04 Lifecycle

- [ ] Implement state machine
- [ ] Validate transitions
- [ ] Record history per change
- [ ] Prevent invalid states

FR-05 Assignee assignment

- [ ] Assign assignee endpoint
- [ ] Validate active assignee
- [ ] Record history

FR-06 Auditable history

- [ ] History table
- [ ] Record action
- [ ] Record user
- [ ] Record date
- [ ] Record observations

FR-07 Request queries

- [ ] Filter by status endpoint
- [ ] Filter by priority
- [ ] Filter by type
- [ ] Filter by assignee
- [ ] Pagination

FR-08 Request closure

- [ ] Validate previous state
- [ ] Require observation
- [ ] Block subsequent editing

🔹 REST API (FR-12)

- [ ] REST controllers
- [ ] Request/response DTOs
- [ ] Bean Validation
- [ ] Correct HTTP status codes
- [ ] Standard error handling
- [ ] Swagger/OpenAPI documentation

🔹 Basic Security (FR-13)

- [ ] Basic or mock JWT authentication
- [ ] Defined roles
- [ ] Endpoint restrictions
- [ ] User audit

🔹 Optional AI

- [ ] Implement FR-09 or FR-10
- [ ] Create AI service interface
- [ ] Fallback without AI (FR-11)

🔹 Testing

- [ ] Service unit tests
- [ ] Repository tests
- [ ] API integration tests
- [ ] Priority rules tests
- [ ] State machine tests

🔹 Code Quality

- [ ] Weekly code review
- [ ] Checkstyle / Sonar
- [ ] JavaDoc documentation
- [ ] Backend technical README

4️⃣ Internal Sprint Planning
Week 6 – Base Setup

- [ ] Create project
- [ ] DB + ORM
- [ ] Main entities
- [ ] Migrations

Week 7 – FR-01 to FR-03

- [ ] Request registration
- [ ] Classification
- [ ] Prioritization

Week 8 – FR-04 to FR-06

- [ ] State machine
- [ ] Assignee assignment
- [ ] Auditable history

Week 9 – FR-07 to FR-08 + Security

- [ ] Filter queries
- [ ] Request closure
- [ ] Basic roles
- [ ] Swagger

Week 10 – Testing and Refactoring

- [ ] Integration tests
- [ ] Fix bugs
- [ ] Documentation
- [ ] Backend demo

5️⃣ Definition of Done (Milestone 2)

The backend is ready when:

- [ ] All backend FRs work
- [ ] API responds without critical errors
- [ ] States are correctly validated
- [ ] Auditable history is complete
- [ ] Tests pass
- [ ] Basic security is functional
- [ ] Works without AI

6️⃣ Milestone Deliverables

- [ ] Functional backend repository
- [ ] Persistent database
- [ ] Documented Swagger
- [ ] Basic tests
- [ ] Endpoint demos
