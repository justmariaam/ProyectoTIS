# Sistema de Control de Acceso de Estacionamiento (SICAE)

## Descripción

SICAE es una solución basada en arquitectura de microservicios para la gestión de usuarios, vehículos y control de acceso a un estacionamiento privado.

El sistema está compuesto por tres dominios independientes:

- Usuarios y autenticación
- Vehículos
- Estacionamiento

Cada microservicio administra su propia base de datos y se comunica mediante APIs REST.


## Despliegue

- Docker
- Docker Compose

## Pruebas

- Postman

---

# Estructura del Proyecto

```text
SICAE-TEST/

├── usuarios/
│   ├── scripts/
│   ├── sicae-usuarios/
│   └── docker-compose.yml
│
├── vehiculos/
│   ├── scripts/
│   ├── sicae-vehiculos/
│   └── docker-compose.yml
│
└── estacionamiento/
    ├── scripts/
    ├── sicae-estacionamiento/
    └── docker-compose.yml
    
```

Cada dominio contiene:

- Código fuente del microservicio
- Dockerfile
- Scripts de base de datos
- docker-compose.yml

---

# Requisitos previos

- Java JDK 17 o superior
- Apache Maven
- Docker Desktop
- Apache NetBeans

---

# Compilación de los microservicios

Abrir cada proyecto en NetBeans o VisualStudio y ejecutar:

```text
Clean and Build
```

Realizar este proceso para:

- sicae-usuarios
- sicae-vehiculos
- sicae-estacionamiento

---

# Crear la red Docker

Crear la red compartida utilizada por los microservicios:

```bash
docker network create sicae-network
```

Este comando solo se ejecuta una vez.

---

# Levantar el microservicio de Usuarios

```bash
cd SICAE-TEST/usuarios
docker compose up -d --build
```

Contenedores generados:

- sicae-db-usuarios
- sicae-usuarios-app

---

# Levantar el microservicio de Vehículos

```bash
cd SICAE-TEST/vehiculos
docker compose up -d --build
```

Contenedores generados:

- sicae-db-vehiculos
- sicae-vehiculos-app

---

# Levantar el microservicio de Estacionamiento

```bash
cd SICAE-TEST/estacionamiento
docker compose up -d --build
```

Contenedores generados:

- sicae-db-estacionamiento
- sicae-estacionamiento-app

---

# Verificar contenedores

```bash
docker ps
```

Deberán visualizarse los seis contenedores:

- sicae-db-usuarios
- sicae-usuarios-app
- sicae-db-vehiculos
- sicae-vehiculos-app
- sicae-db-estacionamiento
- sicae-estacionamiento-app

# Endpoints principales

## Usuarios

```text
http://localhost:8082
```

## Vehículos

```text
http://localhost:8083
```

## Estacionamiento

```text
http://localhost:8084
```

---
# Pruebas

Las pruebas funcionales fueron realizadas mediante Postman.

Orden recomendado de ejecución:

1. Login
2. Usuarios
3. Vehículos
4. Estacionamiento

Para consumir los servicios protegidos se debe enviar el token JWT obtenido durante el login.

Ejemplo:

```http
Authorization: Bearer <TOKEN>
```