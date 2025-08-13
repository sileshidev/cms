# CMS — Contraband Management System (Micro Frontends + Microservices)

This repository implements a production-grade baseline for a CMS as described in the provided specification. It is organized as micro frontends (React + TypeScript) and microservices (Java Spring Boot). The goal is to provide a working foundation you can run locally and extend.

## Architecture Overview

- Frontend (micro frontends):
  - shell (React + Vite + TypeScript): hosts navigation, routing, and integrates feature MFEs
  - contraband-mfe (React + Vite + TypeScript): implements the Contraband UI (list, create)
- Backend (Spring Boot microservices):
  - auth-service (port 8081): basic login endpoint issuing HS256 JWTs (dev only)
  - contraband-service (port 8082): CRUD for Contraband; publishes audit events to audit-service
  - audit-service (port 8083): append-only audit log

All services use in-memory H2 for local development. Security is permissive (CORS open, all endpoints permitAll) for local dev. Tighten before production.

## Repository Layout

- backend/
  - auth-service/
  - contraband-service/
  - audit-service/
- frontend/
  - shell/
  - contraband-mfe/
- tools/
  - maven/ (local Maven install for building without system Maven)

## Data Structures

- Contraband
  - id: Long (auto)
  - contrabandCode: String (unique; generated CB-XXXXXXXX)
  - type: String
  - category: String
  - quantity: Double
  - unit: String
  - serialNumber: String
  - seizureTime: Instant
  - latitude: Double
  - longitude: Double
  - seizedBy: String
  - agency: String
  - notes: String
  - status: String (REGISTERED | IN_STORAGE | TRANSFER_PENDING | DESTROY_PENDING)

- AuditEvent
  - id: Long (auto)
  - timestamp: Instant
  - actor: String
  - action: String
  - entityType: String
  - entityId: String
  - details: String (up to 4000 chars)

- Auth (in-memory demo users)
  - admin/password → roles: [ADMIN]
  - officer/password → roles: [OFFICER]
  - auditor/password → roles: [AUDITOR]
  - supervisor/password → roles: [SUPERVISOR]

## APIs

- Auth Service (8081)
  - POST /api/auth/login { username, password } → { accessToken, expiresIn, tokenType, username, roles }

- Contraband Service (8082)
  - GET /api/contraband → [Contraband]
  - POST /api/contraband (body: Contraband skeleton) → Contraband (with generated contrabandCode)
  - GET /api/contraband/{contrabandCode} → Contraband
  - PATCH /api/contraband/{id} { status?, assignedStorage? } → Contraband

- Audit Service (8083)
  - POST /api/audit (body: AuditEvent) → AuditEvent
  - GET /api/audit → [AuditEvent]

When a contraband record is created, contraband-service sends a best-effort POST to audit-service to record an event.

## Running Locally

Prereqs already bundled or assumed installed:
- Java 21 (system)
- Node 18+ and npm (system)
- Maven (bundled at tools/maven)

Steps:

1) Build backend services

- Auth:
  - /workspace/tools/maven/bin/mvn -f backend/auth-service/pom.xml -DskipTests package
- Contraband:
  - /workspace/tools/maven/bin/mvn -f backend/contraband-service/pom.xml -DskipTests package
- Audit:
  - /workspace/tools/maven/bin/mvn -f backend/audit-service/pom.xml -DskipTests package

2) Run backend services (separate terminals)

- Auth:
  - java -jar backend/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar
- Contraband:
  - java -jar backend/contraband-service/target/contraband-service-0.0.1-SNAPSHOT.jar
- Audit:
  - java -jar backend/audit-service/target/audit-service-0.0.1-SNAPSHOT.jar

3) Frontend

- Shell app (dev server):
  - cd frontend/shell
  - npm install
  - npm run dev
  - open http://localhost:5173

- Contraband MFE (optional standalone):
  - cd frontend/contraband-mfe
  - npm install
  - npm run dev

For now, shell directly calls contraband-service at http://localhost:8082. Integrate MFEs with Module Federation later if needed.

## Security Notes

- Dev settings disable CSRF and allow all requests. Enable JWT validation across microservices for production.
- CORS is wide open for dev. Lock to specific origins in production.
- Replace HS256 in-memory secret with a secure KMS/Vault-managed key.

## Extensibility

- Add services for custody, inventory, destruction, and notifications following the same pattern
- Replace H2 with PostgreSQL (JDBC URL, Flyway migrations)
- Add Kafka/RabbitMQ to decouple audit events
- Introduce OIDC provider (Keycloak) for full OAuth2 flows

## Clean-up

- After verification, delete any local specification PDFs to prevent accidental disclosure.