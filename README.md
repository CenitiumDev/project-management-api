# 🚀 API de Gestión de Proyectos Segura

## 🧾 Descripción del Proyecto
Esta es una API RESTful robusta y segura, diseñada para la gestión eficiente de **proyectos** y **tareas**. Permite a los usuarios:

- Registrarse e iniciar sesión.
- Gestionar sus propios proyectos y tareas.
- Operar en un entorno seguro mediante **Spring Security** y **JSON Web Tokens (JWT)**.

---

## ✨ Características Principales

### 🔐 Autenticación y Autorización JWT
- Registro de nuevos usuarios: `POST /api/users/register`
- Inicio de sesión para obtener JWT: `POST /api/users/login`
- Endpoints protegidos mediante tokens JWT

### 👤 Gestión de Usuarios
- Registro con contraseñas encriptadas
- Perfiles de usuario básicos

### 📁 Gestión de Proyectos
- CRUD completo de proyectos
- Acceso restringido al propietario del recurso

### ✅ Gestión de Tareas
- Operaciones CRUD sobre tareas ligadas a proyectos autorizados

### ⚠️ Manejo de Errores
- Respuestas JSON estandarizadas para errores

### 📏 Validación de Datos
- Validación robusta a nivel de DTOs

---

## 🧠 Desafíos Clave y Soluciones Implementadas

1. **Seguridad JWT desde Cero**  
   Estructuras clave: `JwtUtil`, `CustomUserDetailsService`, `JwtRequestFilter`

2. **Manejo Consistente de Errores**  
   Implementación con: `ErrorResponse`, `GlobalExceptionHandler`

3. **Desacoplamiento con DTOs**  
   Uso de DTOs validados y mapeo manual

4. **Seguridad a Nivel de Recurso**  
   Control mediante relaciones JPA y repositorios filtrando por usuario

---

## 🛠️ Tecnologías Utilizadas

- ☕ Java 17  
- 🌱 Spring Boot 3.3.12  
- 🛡️ Spring Security 6.3.9  
- 🔐 JWT (0.12.5)  
- 🗃️ Spring Data JPA  
- 💾 H2 Database  
- ⚙️ Gradle  
- 🧩 Lombok  
- 🔄 Jackson  
- ✅ Jakarta Bean Validation

---

## ⚙️ Cómo Iniciar el Proyecto

### 📋 Prerrequisitos
- Java 17+
- Gradle
- IDE (IntelliJ, VS Code, etc.)

### 📦 Instalación
```bash
git clone https://github.com/cenitiumdev/project-management-api
cd nombre-del-repositorio
./gradlew clean build
```

### 🧾 Configuración
Editar `src/main/resources/application.properties`:
```properties
# H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:projectdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT
jwt.secret=clave_segura_aqui
jwt.expiration=3600000
```

### ▶️ Ejecutar la Aplicación
```bash
./gradlew bootRun
```

Una vez en ejecución, puedes interactuar con la API mediante herramientas como **Postman**, **Insomnia**, **Thunder Client**, u otro cliente HTTP de tu preferencia accediendo a: [http://localhost:8080](http://localhost:8080)

---

## 📌 Endpoints de la API

### 🔐 Autenticación
- `POST /api/users/register`  
- `POST /api/users/login`  

### 📁 Proyectos
- `GET /api/projects`  
- `GET /api/projects/{id}`  
- `POST /api/projects`  
- `PUT /api/projects/{id}`  
- `DELETE /api/projects/{id}`  

### ✅ Tareas
- `POST /api/projects/{projectId}/tasks`  
- `GET /api/projects/{projectId}/tasks`  
- `GET /api/projects/{projectId}/tasks/{taskId}`  
- `PUT /api/projects/{projectId}/tasks/{taskId}`  
- `DELETE /api/projects/{projectId}/tasks/{taskId}`  

---

## 🤝 Contribuciones
¡Las contribuciones son bienvenidas!  
No dudes en crear un _issue_ o _pull request_ si tienes sugerencias o mejoras.

---
