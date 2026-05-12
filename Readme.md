# Microservicios con Spring Boot y OpenFeign

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-Spring_Cloud-6DB33F?logo=spring&logoColor=white)

Sistema de dos microservicios construido con Spring Boot que demuestra la comunicación entre servicios mediante **OpenFeign**, persistencia con **JPA/Hibernate**, uso de **DTOs** para desacoplar la capa de presentación de las entidades internas, e infraestructura contenerizada con **Docker Compose**.

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    Cliente (HTTP)                       │
└───────────────────────┬─────────────────────────────────┘
                        │
              ┌─────────▼─────────────┐
              │  ms-openfeign-client  │  :8080
              │  (Servicio Consumidor)│
              │  Spring Boot + Feign  │
              └─────────┬─────────────┘
                        │  OpenFeign (HTTP)
              ┌─────────▼──────────┐
              │   ms-products-api  │  :8081
              │  (Proveedor + BD)  │
              │  Spring Boot + JPA │
              └─────────┬──────────┘
                        │  JDBC
              ┌─────────▼──────────┐
              │      MySQL 8.0     │  :3306
              │  (Volumen Docker)  │
              └────────────────────┘
```

### Descripción de los microservicios

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| **ms-openfeign-client** | `8080` | Consumidor — expone una API REST unificada y delega todas las llamadas a ms-products-api mediante OpenFeign |
| **ms-products-api** | `8081` | Proveedor — gestiona Clientes, Productos y Compras con operaciones CRUD completas respaldadas por MySQL; expone DTOs en lugar de entidades JPA para Clientes y Compras |

---

## Tecnologías

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 21 | Lenguaje |
| Spring Boot | 3.5 | Framework de aplicación |
| Spring Cloud OpenFeign | 2025.0.0 | Cliente HTTP declarativo |
| Spring Data JPA | — | ORM / capa de base de datos |
| Hibernate | — | Implementación de JPA |
| MySQL | 8.0 | Base de datos relacional |
| Lombok | — | Reducción de código repetitivo |
| JUnit 5 + Mockito | — | Pruebas unitarias |
| Docker y Docker Compose | — | Infraestructura |

---

## Estructura del proyecto

```
demo-microservicios/
├── ms-openfeign-client/              # Microservicio consumidor (puerto 8080)
│   └── src/
│       ├── main/java/com/java/
│       │   ├── client/feign/         # Interfaces del cliente Feign
│       │   ├── config/               # Configuración CORS
│       │   ├── controller/           # Controladores REST
│       │   ├── model/                # Modelos espejo de los DTOs del proveedor
│       │   └── service/              # Capa de lógica de negocio
│       └── test/java/com/java/
│           └── service/              # Pruebas unitarias (Mockito)
├── ms-products-api/                  # Microservicio proveedor (puerto 8081)
│   └── src/
│       ├── main/java/com/bd/
│       │   ├── config/               # Configuración de Jackson / Hibernate
│       │   ├── controller/           # Controladores REST
│       │   ├── dto/                  # DTOs de entrada y salida (ClientDTO, PurchaseDTO)
│       │   ├── exception/            # Manejo global de excepciones
│       │   ├── model/                # Entidades JPA (no expuestas directamente)
│       │   ├── repository/           # Repositorios de Spring Data con consultas JPQL
│       │   └── service/              # Lógica de negocio y conversión entidad ↔ DTO
│       └── test/java/com/bd/
│           └── service/              # Pruebas unitarias (Mockito)
├── docker-compose.yml                # Configuración del contenedor MySQL
├── .env.example                      # Plantilla de variables de entorno
└── mysql_query.sql                   # Scripts DDL de base de datos
```

---

## DTOs en ms-products-api

`ms-products-api` utiliza **Data Transfer Objects (DTOs)** como contrato único de entrada y salida en sus endpoints, evitando exponer las entidades JPA y sus relaciones internas directamente en las respuestas HTTP.

> **Productos** es el único recurso que sigue usando la entidad directamente, ya que no tiene relaciones bidireccionales que puedan causar problemas de serialización.

### ClientDTO — `/api/clientes`

El mismo `ClientDTO` se usa tanto para recibir el cuerpo de la petición (`@RequestBody`) como para devolver la respuesta. El campo `purchases` se ignora en la escritura gracias a `@JsonProperty(access = READ_ONLY)`.

| Campo | Tipo | Entrada | Salida |
|---|---|---|---|
| `idClient` | `Integer` | — | ✅ ID generado por la BD |
| `name` | `String` | ✅ | ✅ |
| `email` | `String` | ✅ | ✅ |
| `phone` | `String` | ✅ | ✅ |
| `purchases` | `List<PurchaseDTO>` | ❌ ignorado | ✅ compras del cliente |

### PurchaseDTO — `/api/compras`

El mismo `PurchaseDTO` se usa para entrada y salida. Al crear o actualizar una compra, se leen `idClient` y `products`; los campos `idPurchase`, `clientName` y `quantity` se calculan en el servicio y se devuelven en la respuesta.

| Campo | Tipo | Entrada | Salida |
|---|---|---|---|
| `idPurchase` | `Integer` | — | ✅ ID generado por la BD |
| `idClient` | `Integer` | ✅ ID del cliente | ✅ |
| `clientName` | `String` | — | ✅ nombre del cliente |
| `products` | `List<Product>` | ✅ solo se necesita `id` | ✅ objetos completos |
| `quantity` | `int` | — | ✅ calculado automáticamente |

### Mapeo entidad → DTO (capa de servicio)

La conversión se realiza en `ClientService` y `PurchaseService` mediante métodos `convertClientToDto` / `convertPurchaseToDto`, sin librerías externas de mapeo.

```
Client (entidad)            →  ClientDTO
  id          (Integer)     →    idClient
  name        (String)      →    name
  email       (String)      →    email
  phoneNumber (String)      →    phone          ← nombre de campo diferente
  purchases   (Set<Purchase>) →  purchases (List<PurchaseDTO>)

Purchase (entidad)          →  PurchaseDTO
  id          (Integer)     →    idPurchase     ← nombre de campo diferente
  client.id   (Integer)     →    idClient       ← relación aplanada
  client.name (String)      →    clientName     ← relación aplanada
  products    (List)        →    products
  quantity    (int)         →    quantity
```

---

## Primeros pasos

### Requisitos previos

- Java 21+
- Maven 3.9+
- Docker Desktop

### 1. Clonar el repositorio

```bash
git clone https://github.com/maubmz/demo-microservicios.git
cd demo-microservicios
```

### 2. Configurar las variables de entorno

```bash
cp .env.example .env
# Edita el archivo .env con los valores deseados
```

Variables del archivo `.env`:

```env
MYSQL_ROOT_PASSWORD=contraseña_segura
MYSQL_DATABASE=client_product
CONTAINER_NAME=bd-openfeign
```

### 3. Levantar la base de datos

```bash
docker compose up -d
```

La instancia de MySQL estará disponible en `localhost:3306`.

### 4. Ejecutar los microservicios

Inicia primero **ms-products-api** (proveedor):

```bash
cd ms-products-api
./mvnw spring-boot:run
```

Luego inicia **ms-openfeign-client** (consumidor):

```bash
cd ms-openfeign-client
./mvnw spring-boot:run
```

---

## Endpoints de la API

Todos los ejemplos apuntan a `ms-openfeign-client` en el puerto `8080`. También puedes llamar directamente a `ms-products-api` en el puerto `8081`; ambos servicios devuelven el mismo formato de respuesta.

### Clientes

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/clientes` | Obtener todos los clientes |
| `GET` | `/api/clientes/{id}` | Obtener cliente por ID |
| `POST` | `/api/clientes` | Crear un nuevo cliente |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente por ID |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente por ID |

**Ejemplo para crear un cliente:**

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@ejemplo.com",
    "phone": "5551234567"
  }'
```

**Respuesta:**

```json
{
  "idClient": 1,
  "name": "Juan Pérez",
  "email": "juan@ejemplo.com",
  "phone": "5551234567",
  "purchases": []
}
```

### Productos

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/productos` | Obtener todos los productos |
| `GET` | `/api/productos/{id}` | Obtener producto por ID |
| `POST` | `/api/productos` | Crear un nuevo producto |
| `PUT` | `/api/productos/{id}` | Actualizar producto por ID |
| `DELETE` | `/api/productos/{id}` | Eliminar producto por ID |

**Ejemplo para crear un producto:**

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 1500.00,
    "stock": 10
  }'
```

**Respuesta:**

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1500.0,
  "stock": 10
}
```

### Compras

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/compras` | Obtener todas las compras |
| `GET` | `/api/compras/{id}` | Obtener compra por ID |
| `POST` | `/api/compras` | Crear una nueva compra |
| `PUT` | `/api/compras/{id}` | Actualizar compra por ID |
| `DELETE` | `/api/compras/{id}` | Eliminar compra por ID |

**Ejemplo para crear una compra:**

```bash
curl -X POST http://localhost:8080/api/compras \
  -H "Content-Type: application/json" \
  -d '{
    "idClient": 1,
    "products": [{ "id": 1 }]
  }'
```

**Respuesta:**

```json
{
  "idPurchase": 1,
  "idClient": 1,
  "clientName": "Juan Pérez",
  "products": [
    { "id": 1, "name": "Laptop", "price": 1500.0, "stock": 9 }
  ],
  "quantity": 1
}
```

> **Nota:** `quantity` se calcula automáticamente a partir del número de productos enviados. El campo `clientName` y el stock actualizado de los productos son calculados por el servicio.

---

## Ejecución de pruebas

```bash
# Pruebas para ms-openfeign-client
cd ms-openfeign-client
./mvnw test

# Pruebas para ms-products-api
cd ms-products-api
./mvnw test
```

Las pruebas utilizan **JUnit 5** y **Mockito** para testear la capa de servicio de forma aislada, sin requerir una base de datos activa ni otros servicios en ejecución.

---

## Decisiones de diseño

- **DTOs como contrato único de entrada y salida** — `ms-products-api` usa el mismo DTO para recibir peticiones y enviar respuestas en Clientes y Compras. Esto evita exponer las entidades JPA, previene referencias circulares y permite cambiar el modelo interno sin romper el contrato HTTP con los consumidores.
- **Modelos espejo en el consumidor** — Los modelos de `ms-openfeign-client` (`Client`, `Purchase`) replican exactamente los campos de los DTOs del proveedor, garantizando que OpenFeign pueda deserializar correctamente las respuestas.
- **OpenFeign vs RestTemplate** — Cliente HTTP declarativo que elimina código repetitivo, mejora la legibilidad e integra de forma nativa con Spring Cloud.
- **Inyección por constructor** — Todas las dependencias se inyectan mediante constructor (sin `@Autowired` en campo) para mejorar la testeabilidad e inmutabilidad.
- **Separación de responsabilidades** — El consumidor y el proveedor son aplicaciones Spring Boot independientes; cada una puede desplegarse y escalarse de forma autónoma.
- **Configuración basada en variables de entorno** — Las credenciales de la base de datos y nombres de contenedor se externalizan mediante `.env` / variables de entorno Docker, manteniendo los secretos fuera del control de versiones.
- **Carga diferida (Lazy Fetching)** — Las relaciones JPA utilizan `FetchType.LAZY` para evitar el problema N+1. Consultas JPQL con `JOIN FETCH` se usan explícitamente cuando se necesitan los datos relacionados.
- **Configuración de Jackson/Hibernate** — Se registra el módulo `Hibernate6Module` para manejar correctamente las colecciones lazy de JPA durante la serialización JSON, evitando errores de inicialización.
