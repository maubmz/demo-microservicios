# Microservices with Spring Boot & OpenFeign

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-Spring_Cloud-6DB33F?logo=spring&logoColor=white)

A two-microservice system built with Spring Boot demonstrating inter-service communication via **OpenFeign**, persistence with **JPA/Hibernate**, and containerized infrastructure with **Docker Compose**.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                      Client (HTTP)                      │
└───────────────────────┬─────────────────────────────────┘
                        │
              ┌─────────▼──────────┐
              │  ms-openfeign-client│  :8080
              │  (Consumer Service) │
              │  Spring Boot + Feign│
              └─────────┬──────────┘
                        │  OpenFeign (HTTP)
              ┌─────────▼──────────┐
              │   ms-products-api  │  :8081
              │   (Provider + DB)  │
              │   Spring Boot + JPA│
              └─────────┬──────────┘
                        │  JDBC
              ┌─────────▼──────────┐
              │      MySQL 8.0     │  :3306
              │   (Docker Volume)  │
              └────────────────────┘
```

### Microservices

| Service | Port | Responsibility |
|---|---|---|
| **ms-openfeign-client** | `8080` | Consumer — exposes a unified REST API and delegates all calls to ms-products-api via OpenFeign |
| **ms-products-api** | `8081` | Provider — manages Clients, Products, and Purchases with full CRUD operations backed by MySQL |

---

## Technologies

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 3.5 | Application framework |
| Spring Cloud OpenFeign | 2025.0.0 | Declarative HTTP client |
| Spring Data JPA | — | ORM / database layer |
| Hibernate | — | JPA implementation |
| MySQL | 8.0 | Relational database |
| Lombok | — | Boilerplate reduction |
| JUnit 5 + Mockito | — | Unit testing |
| Docker & Docker Compose | — | Infrastructure |

---

## Project Structure

```
Escuela/
├── ms-openfeign-client/          # Consumer microservice (port 8080)
│   └── src/
│       ├── main/java/com/java/
│       │   ├── client/feign/     # Feign client interfaces
│       │   ├── controller/       # REST controllers
│       │   ├── model/            # DTOs / domain models
│       │   └── service/          # Business logic layer
│       └── test/java/com/java/
│           └── service/          # Unit tests (Mockito)
├── ms-products-api/              # Provider microservice (port 8081)
│   └── src/
│       ├── main/java/com/bd/
│       │   ├── config/           # Jackson / app configuration
│       │   ├── controller/       # REST controllers
│       │   ├── exception/        # Global exception handling
│       │   ├── model/            # JPA entities
│       │   ├── repository/       # Spring Data repositories
│       │   └── service/          # Business logic layer
│       └── test/java/com/bd/
│           └── service/          # Unit tests (Mockito)
├── docker-compose.yml            # MySQL container setup
├── .env.example                  # Environment variable template
└── mysql_query.sql               # Database DDL scripts
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker Desktop

### 1. Clone the repository

```bash
git clone https://github.com/your-username/Escuela.git
cd Escuela
```

### 2. Configure environment variables

```bash
cp .env.example .env
# Edit .env with your desired values
```

`.env` variables:

```env
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=client_product
CONTAINER_NAME=bd-openfeign
```

### 3. Start the database

```bash
docker compose up -d
```

The MySQL instance will be available at `localhost:3306`.

### 4. Run the microservices

Start **ms-products-api** first (provider):

```bash
cd ms-products-api
mvn spring-boot:run
```

Then start **ms-openfeign-client** (consumer):

```bash
cd ms-openfeign-client
mvn spring-boot:run
```

---

## API Endpoints

All examples target `ms-openfeign-client` at port `8080`. You can also call `ms-products-api` directly on port `8081`.

### Clients

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/clientes` | Get all clients |
| `GET` | `/api/clientes/{id}` | Get client by ID |
| `POST` | `/api/clientes` | Create a new client |
| `PUT` | `/api/clientes/{id}` | Update client by ID |
| `DELETE` | `/api/clientes/{id}` | Delete client by ID |

**Create client example:**

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "5551234567",
    "purchases": []
  }'
```

### Products

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/productos` | Get all products |
| `GET` | `/api/productos/{id}` | Get product by ID |
| `POST` | `/api/productos` | Create a new product |
| `PUT` | `/api/productos/{id}` | Update product by ID |
| `DELETE` | `/api/productos/{id}` | Delete product by ID |

**Create product example:**

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 1500.00,
    "stock": 10
  }'
```

### Purchases

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/compras` | Get all purchases |
| `GET` | `/api/compras/{id}` | Get purchase by ID |
| `POST` | `/api/compras` | Create a new purchase |
| `PUT` | `/api/compras/{id}` | Update purchase by ID |
| `DELETE` | `/api/compras/{id}` | Delete purchase by ID |

**Create purchase example:**

```bash
curl -X POST http://localhost:8080/api/compras \
  -H "Content-Type: application/json" \
  -d '{
    "client": { "id": 1 },
    "products": [{ "id": 1 }]
  }'
```

---

## Running Tests

```bash
# Run tests for ms-openfeign-client
cd ms-openfeign-client
mvn test

# Run tests for ms-products-api
cd ms-products-api
mvn test
```

Tests use **JUnit 5** and **Mockito** to unit-test the service layer in isolation, without requiring a running database or other services.

---

## Key Design Decisions

- **OpenFeign over RestTemplate** — Declarative HTTP client that eliminates boilerplate, improves readability, and integrates natively with Spring Cloud.
- **Constructor Injection** — All dependencies are injected via constructor (not `@Autowired` field injection) for better testability and immutability.
- **Separation of Concerns** — Consumer and provider are independent Spring Boot applications; each can be deployed and scaled independently.
- **Environment-based Configuration** — Database credentials and container names are externalized via `.env` / Docker environment variables, keeping secrets out of source control.
- **Lazy Fetching** — JPA relationships use `FetchType.LAZY` to avoid N+1 query problems and unnecessary data loading.
