# API de Gestión de Proyectos Segura

## Descripción del Proyecto
Este proyecto es una API RESTful robusta y segura diseñada para la gestión eficiente de proyectos y tareas. Permite a los usuarios registrarse, iniciar sesión y realizar operaciones CRUD sobre sus propios proyectos y las tareas asociadas a ellos. La seguridad es un pilar fundamental, implementada mediante Spring Security y JSON Web Tokens (JWT) para la autenticación y autorización.

## Características Principales
- **Autenticación y Autorización JWT**
  - Registro de nuevos usuarios (`/api/users/register`)
  - Inicio de sesión para obtener tokens JWT (`/api/users/login`)
  - Protección de endpoints con autenticación basada en tokens JWT

- **Gestión de Usuarios**
  - Registro de usuarios con contraseñas encriptadas
  - Perfiles de usuario básicos

- **Gestión de Proyectos**
  - Creación, visualización, actualización y eliminación de proyectos
  - Control de acceso por usuario propietario

- **Gestión de Tareas**
  - CRUD de tareas asociadas a proyectos autorizados

- **Manejo de Errores Consistente**
  - Respuestas de error JSON estandarizadas

- **Validación de Datos**
  - Validación de entrada a nivel de DTO

## Desafíos Clave y Soluciones Implementadas
1. **Seguridad JWT desde Cero**
   - `JwtUtil`, `CustomUserDetailsService`, `JwtRequestFilter`

2. **Manejo Consistente de Errores**
   - `ErrorResponse`, `GlobalExceptionHandler`

3. **Errores JWT (401 vs 403)**
   - `JwtAuthenticationEntryPoint`, manejo en `JwtRequestFilter`

4. **Desacoplamiento con DTOs**
   - DTOs validados, mapeo manual

5. **Seguridad a Nivel de Recurso**
   - Relaciones JPA, métodos de repositorio filtrando por usuario

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.3.12
- Spring Security 6.3.9
- JWT (0.12.5)
- Spring Data JPA
- H2 Database
- Gradle
- Lombok
- Jackson
- Jakarta Bean Validation

## Cómo Iniciar el Proyecto

### Prerrequisitos
- Java 17+
- Gradle
- IDE (IntelliJ, VS Code, etc.)

### Instalación
```bash
git clone https://github.com/tu-usuario/nombre-del-repositorio.git
cd nombre-del-repositorio
./gradlew clean build
```

### Configuración
`src/main/resources/application.properties`
```properties
# H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:projectdb
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JWT
jwt.secret=clave_segura_aqui
jwt.expiration=3600000

# Logs (opcional)
logging.level.org.springframework.security=DEBUG
```

### Ejecutar la Aplicación
```bash
./gradlew bootRun
```

Accede a: `http://localhost:8080`

## Endpoints de la API

### Autenticación
- `POST /api/users/register`
- `POST /api/users/login`

### Proyectos
- `GET /api/projects`
- `GET /api/projects/{id}`
- `POST /api/projects`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`

### Tareas
- `POST /api/projects/{projectId}/tasks`
- `GET /api/projects/{projectId}/tasks`
- `GET /api/projects/{projectId}/tasks/{taskId}`
- `PUT /api/projects/{projectId}/tasks/{taskId}`
- `DELETE /api/projects/{projectId}/tasks/{taskId}`

## Contribuciones
¡Contribuciones son bienvenidas! Crea un issue o pull request si tienes sugerencias.
