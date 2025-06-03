API de Gestión de Proyectos Segura
Descripción del Proyecto

Este proyecto es una API RESTful robusta y segura diseñada para la gestión eficiente de proyectos y tareas. Permite a los usuarios registrarse, iniciar sesión y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre sus propios proyectos y las tareas asociadas a ellos. La seguridad es un pilar fundamental, implementada mediante Spring Security y JSON Web Tokens (JWT) para la autenticación y autorización.
Características Principales

    Autenticación y Autorización JWT:

        Registro de nuevos usuarios (/api/users/register).

        Inicio de sesión para obtener tokens JWT (/api/users/login).

        Protección de endpoints con autenticación basada en tokens JWT.

    Gestión de Usuarios:

        Registro de usuarios con contraseñas encriptadas.

        Perfiles de usuario básicos.

    Gestión de Proyectos:

        Creación de nuevos proyectos, asociados a un usuario propietario.

        Visualización de proyectos (individual y listado), asegurando que solo el usuario propietario pueda acceder a los suyos.

        Actualización de detalles de proyectos.

        Eliminación de proyectos.

    Gestión de Tareas:

        Creación de nuevas tareas, asociadas a un proyecto específico.

        Visualización de tareas (individual y listado), asegurando que solo se accedan a tareas dentro de proyectos autorizados.

        Actualización de detalles y estado de tareas.

        Eliminación de tareas.

    Manejo de Errores Consistente:

        Respuestas de error JSON estandarizadas para facilitar la integración con el cliente.

        Manejo avanzado de errores de autenticación JWT.

    Validación de Datos:

        Validación de entrada de datos a nivel de DTO para asegurar la integridad de la información.

Desafíos Clave y Soluciones Implementadas

Durante el desarrollo de esta API, se abordaron varios desafíos técnicos cruciales para construir un sistema robusto, seguro y mantenible:
1. Implementación de Seguridad JWT desde Cero

    Desafío: Integrar un sistema de autenticación y autorización sin estado (stateless) utilizando JWT con Spring Security, requiriendo la creación de componentes personalizados como filtros y utilidades de token.

    Solución:

        JwtUtil: Clase de utilidad para la generación, validación y extracción de información de los JWT.

        CustomUserDetailsService: Servicio personalizado para cargar los detalles del usuario, integrándose con Spring Security.

        JwtRequestFilter: Un filtro que intercepta las peticiones HTTP, extrae y valida el JWT, y establece el contexto de seguridad de Spring.

2. Manejo Consistente de Errores y Excepciones

    Desafío: Evitar respuestas de error genéricas (ej. páginas HTML de error o 500 Internal Server Error) y proporcionar mensajes de error claros y estructurados en formato JSON para el cliente.

    Solución:

        ErrorResponse DTO: Definición de un formato de respuesta JSON estandarizado para los errores.

        GlobalExceptionHandler (@ControllerAdvice): Implementación de un controlador de excepciones global para interceptar y manejar diferentes tipos de excepciones (ej. ResourceNotFoundException, MethodArgumentNotValidException, BadCredentialsException) y devolver el ErrorResponse adecuado con el código de estado HTTP correcto.

3. Manejo Específico de Errores de Autenticación JWT (401 Unauthorized vs 403 Forbidden)

    Desafío: Asegurar que los problemas relacionados con JWT (token inválido, expirado, mal formado) resulten en un 401 Unauthorized con un cuerpo JSON detallado, en lugar del 403 Forbidden por defecto de Spring Security (que no proporciona información útil).

    Solución:

        JwtAuthenticationEntryPoint Personalizado: Se configuró un AuthenticationEntryPoint para manejar los fallos de autenticación, devolviendo un 401 Unauthorized con el ErrorResponse JSON.

        Manejo Activo en JwtRequestFilter: El JwtRequestFilter fue modificado para capturar explícitamente las excepciones de la librería JJWT (ej. ExpiredJwtException, MalformedJwtException). Al capturarlas, el filtro invoca directamente al JwtAuthenticationEntryPoint y detiene la cadena de filtros (return;), previniendo que Spring Security genere un 403 genérico y asegurando la respuesta 401 deseada.

4. Desacoplamiento de Entidades y API con DTOs

    Desafío: Mantener una clara separación entre el modelo de datos de la base de datos (entidades JPA) y la representación de datos expuesta por la API, así como controlar la validación de entrada.

    Solución:

        Creación de DTOs (ProjectDTO, TaskDTO): Se definieron DTOs específicos para las operaciones de creación, actualización y respuesta, permitiendo un control granular sobre los campos expuestos y recibidos.

        Validación de Entrada (@Valid): Se utilizaron anotaciones de validación de Jakarta Bean Validation (@NotBlank, @NotNull, @Size) en los DTOs para asegurar que los datos de entrada cumplan con los requisitos antes de ser procesados por los servicios.

        Mapeo Manual: Implementación de métodos de mapeo entre entidades y DTOs en los controladores para la conversión de datos.

5. Seguridad a Nivel de Recurso (Propiedad de Datos)

    Desafío: Garantizar que un usuario solo pueda acceder, modificar o eliminar sus propios proyectos y las tareas asociadas a ellos, evitando el acceso no autorizado a datos de otros usuarios.

    Solución:

        Relaciones JPA: Establecimiento de relaciones @ManyToOne entre Project y User, y entre Task y Project.

        Métodos de Repositorio Personalizados: Creación de métodos en ProjectRepository (ej. findByOwner, findByIdAndOwner) y TaskRepository (ej. findByProject, findByIdAndProject) que incorporan la verificación del propietario o del proyecto asociado.

        Lógica de Servicio: Los servicios (ProjectService, TaskService) siempre obtienen el usuario autenticado (@AuthenticationPrincipal) y utilizan estos métodos de repositorio para filtrar y validar el acceso a los recursos, lanzando ResourceNotFoundException si el recurso no existe o no pertenece al usuario.

Tecnologías Utilizadas

    Spring Boot 3.3.12: Framework para el desarrollo rápido de aplicaciones Java.

    Spring Security 6.3.9: Para seguridad robusta, autenticación y autorización.

    JWT (JSON Web Tokens) 0.12.5: Para autenticación sin estado.

    Spring Data JPA: Para la capa de persistencia con bases de datos.

    H2 Database: Base de datos en memoria para desarrollo y pruebas.

    Gradle: Herramienta de automatización de construcción.

    Java 17: Lenguaje de programación principal.

    Lombok: Para reducir el boilerplate code (getters, setters, constructores).

    Jackson (ObjectMapper): Para serialización/deserialización JSON.

    Jakarta Bean Validation: Para la validación de datos en DTOs.

Cómo Iniciar el Proyecto
Prerrequisitos

    Java 17 (o superior)

    Gradle (generalmente incluido con el wrapper de Spring Boot)

    Un IDE (IntelliJ IDEA, VS Code, Eclipse)

Instalación

    Clona el repositorio:

    git clone [https://github.com/tu-usuario/nombre-del-repositorio.git](https://github.com/tu-usuario/nombre-del-repositorio.git) # Reemplaza con tu URL de repositorio
    cd nombre-del-repositorio

    Construye el proyecto:

    ./gradlew clean build

Configuración (application.properties)

Asegúrate de que tu src/main/resources/application.properties (o .yml) contenga las configuraciones básicas para la base de datos H2 y JWT. Es crucial que jwt.secret sea una cadena larga y segura.

# Configuración H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:projectdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update # o create para limpiar en cada inicio

# Configuración JWT
jwt.secret=QzP3tVXSf9t2K6n8WbZcMdEfGjHlQ1rY0uXaCvYgO7iN5mPkLqR4sT3wV2yU1xZ0FjBpHyJkR7nWmXoAqLcVwS6uY8t1aZb0cKdGeHgIfIjMkNpOqRrStUvWxYzAbCdEfG # Usa la clave que generamos
jwt.expiration=3600000 # 1 hora en milisegundos (ajusta según necesites)

# Logs de depuración (opcional, para ver la cadena de seguridad)
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web.filter.OncePerRequestFilter=DEBUG
logging.level.io.jsonwebtoken=DEBUG
logging.level.co.cenitiumdev.projectmanagementapi=DEBUG

Para entornos de producción, jwt.secret debe ser una cadena generada criptográficamente y gestionada de forma segura (ej. variables de entorno, servicios de secretos).
Ejecutar la Aplicación

./gradlew bootRun

La aplicación se ejecutará en http://localhost:8080.
Endpoints de la API

Aquí hay algunos endpoints clave que puedes probar (se recomienda usar Postman o un cliente REST como Insomnia):
Autenticación

    POST /api/users/register

        Descripción: Crea un nuevo usuario en el sistema.

        Body (JSON): {"username": "nuevo_usuario", "password": "una_password_segura", "email": "correo@ejemplo.com"}

        Respuesta Exitosa: 201 Created con mensaje de éxito.

    POST /api/users/login

        Descripción: Autentica un usuario y devuelve un token JWT para futuras peticiones.

        Body (JSON): {"username": "usuario_existente", "password": "su_password"}

        Respuesta Exitosa: 200 OK con el token JWT en el cuerpo (ej.: "eyJhbGciOiJIUzI1NiJ9...").

Proyectos (Requiere token JWT en el header Authorization: Bearer <token>)

    GET /api/projects

        Descripción: Obtiene una lista de todos los proyectos pertenecientes al usuario autenticado.

        Respuesta Exitosa: 200 OK con un array de ProjectDTO.

    GET /api/projects/{id}

        Descripción: Obtiene los detalles de un proyecto específico por su ID, si pertenece al usuario autenticado.

        Respuesta Exitosa: 200 OK con un ProjectDTO.

    POST /api/projects

        Descripción: Crea un nuevo proyecto para el usuario autenticado.

        Body (JSON): {"name": "Nuevo Proyecto", "description": "Descripción del proyecto.", "startDate": "2025-06-01", "endDate": "2025-12-31"}

        Respuesta Exitosa: 201 Created con el ProjectDTO del proyecto creado.

    PUT /api/projects/{id}

        Descripción: Actualiza un proyecto existente por su ID, si pertenece al usuario autenticado.

        Body (JSON): {"name": "Proyecto Actualizado", "description": "Descripción actualizada.", "startDate": "2025-06-01", "endDate": "2025-12-31"}"

        Respuesta Exitosa: 200 OK con el ProjectDTO del proyecto actualizado.

    DELETE /api/projects/{id}

        Descripción: Elimina un proyecto por su ID, si pertenece al usuario autenticado.

        Respuesta Exitosa: 204 No Content.

Tareas (Requiere token JWT en el header Authorization: Bearer <token>)

    POST /api/projects/{projectId}/tasks

        Descripción: Crea una nueva tarea para un proyecto específico del usuario autenticado.

        Body (JSON): {"name": "Nueva Tarea", "description": "Descripción de la tarea.", "dueDate": "2025-07-15", "status": "PENDING"}

        Respuesta Exitosa: 201 Created con el TaskDTO de la tarea creada.

    GET /api/projects/{projectId}/tasks

        Descripción: Obtiene todas las tareas asociadas a un proyecto específico del usuario autenticado.

        Respuesta Exitosa: 200 OK con un array de TaskDTO.

    GET /api/projects/{projectId}/tasks/{taskId}

        Descripción: Obtiene los detalles de una tarea específica por su ID, si pertenece al proyecto y usuario autenticado.

        Respuesta Exitosa: 200 OK con un TaskDTO.

    PUT /api/projects/{projectId}/tasks/{taskId}

        Descripción: Actualiza una tarea existente por su ID, si pertenece al proyecto y usuario autenticado.

        Body (JSON): {"name": "Tarea Actualizada", "description": "Nueva descripción.", "dueDate": "2025-07-20", "status": "IN_PROGRESS"}

        Respuesta Exitosa: 200 OK con el TaskDTO de la tarea actualizada.

    DELETE /api/projects/{projectId}/tasks/{taskId}

        Descripción: Elimina una tarea por su ID, si pertenece al proyecto y usuario autenticado.

        Respuesta Exitosa: 204 No Content.