SHOW DATABASES;

CREATE DATABASE client_product;

USE client_product;

CREATE TABLE clients(
    id BIGINT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    email VARCHAR(40) NOT NULL,
    telefono VARCHAR(40) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE products(
    id BIGINT AUTO_INCREMENT,
    nombre VARCHAR(40) NOT NULL,
    precio DECIMAL NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE purchases(
    id BIGINT AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE purchase_products(
    purchase_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_order INT NOT NULL DEFAULT 0,  -- ← agregado para permitir duplicados
    PRIMARY KEY (purchase_id, product_order),  -- ← cambiado de (purchase_id, product_id)
    FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

SHOW TABLES;
