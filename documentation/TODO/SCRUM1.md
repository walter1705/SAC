# MILESTONE 1 – Detailed Scrum

Phase: Design and Modeling (Weeks 1–5)
Focus: Domain, architecture, and API contracts

1️⃣ Milestone Objective

Formally define:

- [x] Domain model
- [x] System architecture
- [x] Lifecycle states
- [x] REST API contracts
- [x] Business rules strategy

Without implementing production code yet.

2️⃣ Milestone Product Goal

Have a validated design that allows starting backend implementation without ambiguities.

3️⃣ Milestone 1 Product Backlog (Derived from FRs)
🔹 Requirements Analysis

- [x] Decompose FR-01 to FR-13
- [x] Identify explicit and implicit rules
- [x] Identify technical constraints
- [x] Define system actors
- [x] Define role-based permissions (basis for FR-13)
- [x] Detect ambiguities in the document

🔹 Domain Modeling

- [x] Identify main entities
- [x] Define mandatory attributes per entity
- [x] Define relationships (1:N, N:M)
- [x] Model Request entity
- [x] Model User entity
- [x] Model Assignee entity
- [x] Model RequestHistory entity
- [x] Model RequestStatus enum
- [x] Model Priority enum
- [x] Define domain invariants

🔹 Lifecycle Modeling (FR-04)

Define states:

- [x] Registered
- [x] Classified
- [x] In Progress
- [x] Resolved
- [x] Closed
- [x] Define valid transitions
- [x] Document invalid transitions
- [x] Define business rules per transition

🔹 Prioritization (FR-03)

- [x] Define academic impact criteria
- [x] Define priority rules
- [x] Document decision logic
- [x] Define structure to justify priority

🔹 Architecture Design

Define layered architecture:

- [x] Controller
- [x] Service
- [x] Repository
- [x] Domain
- [x] Define main pattern
- [x] Define decoupling strategies
- [x] Define global exception handling
- [x] Define validation strategy

🔹 REST API Design (FR-12)

- [x] Define Request CRUD endpoints
- [x] Define classification endpoint
- [x] Define prioritization endpoint
- [x] Define state change endpoint
- [x] Define assignee assignment endpoint
- [x] Define history endpoint
- [x] Define query filters
- [x] Define JSON request/response structure
- [x] Define correct HTTP status codes
- [x] Document API contract

🔹 Base Security (FR-13)

Define roles:

- [x] Student
- [x] Administrative Staff
- [x] Coordinator
- [x] Map permissions to endpoints

🔹 AI (Conceptual Design)

- [x] Define which AI FR will be implemented (09 or 10)
- [x] Design integration point
- [x] Ensure independence (FR-11)
