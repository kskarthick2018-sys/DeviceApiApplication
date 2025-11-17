# Device API

Java + Spring Boot REST API for managing devices.

## Features
- Create / update (PUT/PATCH) / get / delete devices
- Business rules: creation time immutable; cannot update name/brand when device is IN_USE; cannot delete IN_USE devices
- Oracle Database persistence
- Dockerized (app)


## Run locally
1. Start Oracle XE (or use your local Oracle DB)
2. Build and run: `mvn clean install`
3. API available at `http://localhost:8080/api/devices`
4. Swagger UI: `http://localhost:8080/swagger-ui.html`

## Tests
`mvn test`

## Future improvements
- Integration tests with Testcontainers for Oracle
- Authorization & authentication
- Pagination and sorting for GET /devices
- Soft-delete instead of hard delete
- CI pipeline (GitHub Actions reusable workflkow)
