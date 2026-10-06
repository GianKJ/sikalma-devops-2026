# Iteración 1 Arquitectura inicial

## Alcance completado

Esta iteración crea una base ejecutable para el sistema SIKALMA. Incluye la aplicación Spring Boot,
la estructura de paquetes, el sitio público inicial, el formulario de acceso, la seguridad de rutas,
la configuración de SQL Server y pruebas de arranque.

El proyecto académico suministrado se utilizó únicamente como referencia para conservar la separación
MVC y el flujo Controller, Service y Repository. No se copiaron entidades, textos, datos ni vistas.

## Decision de vistas

Se utiliza Thymeleaf en lugar de JSP porque el proyecto es nuevo, se ejecutará como archivo JAR con
servidor embebido y debe mantenerse sobre Spring Boot 3 y Jakarta. Esta decisión evita la configuración
adicional de JSP y WAR sin cambiar el patrón Spring MVC solicitado.

## Capas

- `controller`: recibe solicitudes HTTP y prepara las vistas.
- `service`: concentra casos de uso y reglas de negocio.
- `repository`: define el acceso a datos mediante Spring Data JPA.
- `entity`: contiene el modelo persistente.
- `dto`: transporta datos entre capas y formularios.
- `security`: autenticación, autorización y protecciones web.
- `validation`: validaciones reutilizables.
- `notification`: contratos desacoplados para canales de notificación.
- `audit`: trazabilidad de acciones y cambios.
- `exception`: errores de negocio y respuestas controladas.

## Seguridad inicial

- BCrypt con costo 12 para futuras contrasenas.
- CSRF habilitado por Spring Security.
- Renovación del identificador de sesión al autenticar.
- Una sesión simultánea por cuenta como configuración inicial.
- Rutas separadas para administración, recepción y profesionales.
- Política de seguridad de contenido para recursos propios.

La autenticación contra usuarios de SQL Server se implementará en la Iteración 3, después de definir
el modelo de datos en la Iteracion 2.

## Datos y perfiles

El perfil `dev` no inicializa persistencia. El perfil `sqlserver` apunta a `localhost:1433`, base
`sikalma_db` y usuario `sikalma_user`. La contrasena se obtiene de `SIKALMA_DB_PASSWORD`.

La aplicación no crea ni modifica tablas en esta iteración. `ddl-auto=validate` impedirá que Hibernate
cambie el esquema cuando se active SQL Server.

## Proximos pasos

1. Diseñar el modelo físico y las migraciones de SQL Server.
2. Crear roles, usuarios y autenticación persistente.
3. Implementar profesionales, especialidades y servicios administrables.
4. Incorporar pacientes, disponibilidad y reglas de citas.
