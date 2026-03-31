# Microservicios en Java

## Descripción

Este proyecto tiene como objetivo implementar dos microservicios en Java.
El microservicio B se conecta a una base de datos MySQL y gestiona dos entidades principales:

* Clientes
* Productos

Incluye operaciones CRUD completas.

---

## Tecnologías

* Java 21
* Maven 3.9.9
* Spring Boot
* MySQL 8
* Docker & Docker Compose

---

## Base de datos (MySQL con Docker)

Este proyecto utiliza Docker Compose para levantar la base de datos.

### Requisitos

* Docker Desktop instalado

---

## ▶Levantar la base de datos

```bash
docker compose up -d
```

---

## Configuración de conexión

La base de datos estará disponible en:

```
localhost:3306
```

---

## Variables de entorno

Se definen en el archivo `.env`:

```
MYSQL_ROOT_PASSWORD=********
MYSQL_DATABASE=client_product
MYSQL_USER=root
CONTAINER_NAME=bd-openfeign
```

---

## Configuración en Spring Boot

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/client_product
    username: app_user
    password: app_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

---

## Uso alternativo: Docker CLI

```bash
docker run -d \
  --name bd-openfeign \
  -e MYSQL_ROOT_PASSWORD=*** \
  -e MYSQL_DATABASE=client_product \
  -p 3306:3306 \
  mysql:8.0
```

---

## Estructura del proyecto

```
backend-ProjectB/
── main/
backend-ProjectA/
── main/
docker-compose.yml
.env
README.md
.gitignore
```

---

## Buenas prácticas aplicadas

* Uso de Docker Compose para infraestructura reproducible
* Separación de configuración mediante variables de entorno
* Uso de usuario de aplicación (no root)
* Persistencia de datos con volúmenes
