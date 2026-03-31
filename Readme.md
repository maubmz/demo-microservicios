# Microservicios en Java
El objetivo de este proyecto es generar dos microservicios donde el microservicio B sera un microservicio que se conecte a una BD y genere dos entidades: `Clientes` y `Productos` que se utilizaran en distintas peticiones de un CRUD.

Versiones utilizadas
- Java 21
- Maven 3.9.9
- MYSQL

Comando para generar un contenedor de MySQL que se utilizara para la BD
```
docker run --name bd-openfeign -e MYSQL_ROOT_PASSWORD=M1x_2021 -d mysql
```
