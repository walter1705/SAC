# MILESTONE 1 – Detailed Scrum

Phase: Design and Modeling (Weeks 1–5)
Focus: Domain, architecture, and API contracts

1️⃣ Milestone Objective

Formally define:

- [ ] Domain model
- [ ] System architecture
- [ ] Lifecycle states
- [ ] REST API contracts
- [ ] Business rules strategy

Without implementing production code yet.

2️⃣ Milestone Product Goal

Have a validated design that allows starting backend implementation without ambiguities.

3️⃣ Milestone 1 Product Backlog (Derived from FRs)
🔹 Requirements Analysis

- [ ] Decompose FR-01 to FR-13
- [ ] Identify explicit and implicit rules
- [ ] Identify technical constraints
- [ ] Define system actors
- [ ] Define role-based permissions (basis for FR-13)
- [ ] Detect ambiguities in the document

🔹 Domain Modeling

- [ ] Identify main entities
- [ ] Define mandatory attributes per entity
- [ ] Define relationships (1:N, N:M)
- [ ] Model Request entity
- [ ] Model User entity
- [ ] Model Assignee entity
- [ ] Model RequestHistory entity
- [ ] Model RequestStatus enum
- [ ] Model Priority enum
- [ ] Define domain invariants

🔹 Lifecycle Modeling (FR-04)

Define states:

- [ ] Registered
- [ ] Classified
- [ ] In Progress
- [ ] Resolved
- [ ] Closed
- [ ] Define valid transitions
- [ ] Document invalid transitions
- [ ] Define business rules per transition

🔹 Prioritization (FR-03)

- [ ] Define academic impact criteria
- [ ] Define priority rules
- [ ] Document decision logic
- [ ] Define structure to justify priority

🔹 Architecture Design

Define layered architecture:

- [ ] Controller
- [ ] Service
- [ ] Repository
- [ ] Domain
- [ ] Define main pattern (e.g., Hexagonal or Clean)
- [ ] Define decoupling strategies
- [ ] Define global exception handling
- [ ] Define validation strategy

🔹 REST API Design (FR-12)

- [ ] Define Request CRUD endpoints
- [ ] Define classification endpoint
- [ ] Define prioritization endpoint
- [ ] Define state change endpoint
- [ ] Define assignee assignment endpoint
- [ ] Define history endpoint
- [ ] Define query filters
- [ ] Define JSON request/response structure
- [ ] Define correct HTTP status codes
- [ ] Document API contract

🔹 Base Security (FR-13)

Define roles:

- [ ] Student
- [ ] Administrative Staff
- [ ] Coordinator
- [ ] Define permissions matrix
- [ ] Map permissions to endpoints

🔹 AI (Conceptual Design)

- [ ] Define which AI FR will be implemented (09 or 10)
- [ ] Design integration point
- [ ] Ensure independence (FR-11)

4️⃣ Internal Sprint Planning
Suggested duration:

5 weeks → 5 weekly sprints

Week 1 – Analysis and Decomposition

- [ ] Complete FR analysis
- [ ] Rule identification
- [ ] Actor definition
- [ ] First domain draft

Week 2 – Domain and UML

- [ ] Complete UML
- [ ] States defined
- [ ] Internal technical review

Week 3 – Architecture

- [ ] Layered design
- [ ] Packages
- [ ] Exceptions
- [ ] Rules strategy

Week 4 – REST API

- [ ] Endpoints defined
- [ ] JSON contracts
- [ ] Validations documented

Week 5 – Validation and Consolidation

- [ ] Complete model review
- [ ] Inconsistency adjustments
- [ ] Delivery preparation
- [ ] Conceptual demo

5️⃣ Definition of Done (Milestone 1)

The milestone is considered complete when:

- [ ] UML is consistent
- [ ] States are well defined
- [ ] Transitions are documented
- [ ] Architecture is clear and decoupled
- [ ] REST API is documented
- [ ] Permissions are defined
- [ ] Priority rules are defined
- [ ] No contradictions between FRs

6️⃣ Deliverable Artifacts

- [ ] UML diagram (classes)
- [ ] State diagram
- [ ] Architecture document
- [ ] API contracts document
- [ ] Refined Milestone 2 backlog

7️⃣ Technical Risks Detectable in Milestone 1

- [ ] Anemic model
- [ ] Poorly defined states
- [ ] Transitions without validation
- [ ] Poorly designed API
- [ ] Not considering FR-11 (AI independence)

8️⃣ Control Metrics

- [ ] % FRs modeled
- [ ] No. of ambiguities detected
- [ ] No. of rules documented
- [ ] No. of inconsistencies corrected

9️⃣ Critical Questions to Resolve Before Milestone 2

- [ ] Is the priority derived or editable?
- [ ] Can the assignee reassign?
- [ ] Are regressive states allowed?
- [ ] Is the history immutable?
- [ ] Are the rules hardcoded or configurable?
