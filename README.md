# SW Insurance Service

A Spring Boot microservice for managing insurance information, integrated with a PostgreSQL database and secured with Spring Security.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Local Development](#local-development)
  - [Docker Deployment](#docker-deployment)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Database](#database)
- [Security](#security)
- [Integration](#integration)
- [License](#license)

## Features

- RESTful API for managing insurance information
- Secure endpoints with Basic Authentication
- Integration with external vehicle service
- Database migrations with Flyway
- Containerized with Docker

## Prerequisites

- Java 21
- Maven
- Docker and Docker Compose
- PostgreSQL

## Getting Started

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd sw-insurance
   ```

2. **Configure the database**
   - Create a PostgreSQL database named `ifsw-db`
   - Update the database credentials in `src/main/resources/application.properties` if needed

3. **Build and run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The application will be available at `http://localhost:8081`

### Docker Deployment

1. **Build and start the containers**
   ```bash
   docker-compose up --build -d
   ```

2. **View logs**
   ```bash
   docker-compose logs -f
   ```

3. **Stop the containers**
   ```bash
   docker-compose down
   ```

## API Documentation

### Authentication
All endpoints require Basic Authentication:
- Username: `admin`
- Password: `password`

### Endpoints

#### Get Insurances by Personal ID
```
GET /api/v1/insurances/customer/{personalId}
```

**Parameters**
- `personalId` (path, required): The personal ID of the customer

**Example Request**
```bash
curl -X GET "http://localhost:8081/api/v1/insurances/customer/1234567890" \
     -H "Authorization: Basic YWRtaW46cGFzc3dvcmQ="
```

**Example Response**
```json
[
  {
    "id": 1,
    "policyNumber": "POL-123456",
    "type": "AUTO",
    "startDate": "2025-01-01",
    "endDate": "2026-01-01",
    "status": "ACTIVE"
  }
]
```

## Configuration

### Application Properties

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | 8081 |
| `spring.datasource.url` | Database URL | jdbc:postgresql://localhost:5432/ifsw-db |
| `spring.datasource.username` | Database username | ifsw-user |
| `spring.datasource.password` | Database password | Ifsweden@2025 |
| `vehicle.service.url` | External vehicle service URL | http://localhost:8080 |
| `vehicle.service.path` | Vehicle service API path | /api/v1/vehicles/ |
| `vehicle.service.username` | Vehicle service username | admin |
| `vehicle.service.password` | Vehicle service password | password |

## Database

The application uses Flyway for database migrations. Migration scripts are located in `src/main/resources/db/migration`.

### Schema
- Schema name: `ifsw_schema`
- Tables: (describe your tables here)

## Security

- Basic Authentication is enabled for all endpoints
- Default credentials: admin/password
- CSRF protection is disabled for API endpoints

## Integration

The service integrates with an external vehicle service for vehicle information.

### Vehicle Service Integration
- Base URL: Configurable via `vehicle.service.url`
- Endpoint: `{baseUrl}/api/v1/vehicles/{vehicleId}`
- Authentication: Basic Auth using credentials from configuration

## License

[Your License Here]

---

*This assignment is supoprt by Cascade AI plugin for Intellij IDEA*