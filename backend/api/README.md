# SIKALMA

Sistema web de gestión integral para el Centro Psicológico SIKALMA de Huánuco, Perú.

## Base técnica

- Java 21
- Spring Boot 3.5
- Spring MVC y Thymeleaf
- Spring Security y BCrypt
- Spring Data JPA
- SQL Server
- Maven Wrapper

## Guía de inicio

La guía completa para abrir, configurar y ejecutar el proyecto en Visual Studio Code se encuentra
en [`GUIA_VSCODE.md`](GUIA_VSCODE.md).

El perfil predeterminado `dev` activa también la conexión `sqlserver`. Al iniciar la aplicación,
el script `src/main/resources/db/sqlserver/schema.sql` crea de manera idempotente las tablas que
todavía no existan y Hibernate valida el modelo. Las contraseñas nunca se almacenan en el código.

## Ejecución rápida

Primero crea tu configuración local a partir del ejemplo:

```powershell
Copy-Item .env.example .env
```

Edita `.env` y coloca las credenciales locales. Desde VS Code ejecuta la configuración
`SIKALMA Desarrollo`, o desde PowerShell configura las variables y usa:

```powershell
$env:SIKALMA_DB_PASSWORD="TU_CLAVE_LOCAL"
$env:SPRING_SECURITY_USER_PASSWORD="TU_CLAVE_DE_ACCESO"
.\mvnw.cmd spring-boot:run
```

Luego abre `http://localhost:8080`.

## Pruebas

```powershell
.\mvnw.cmd test
```

## Reglas del repositorio

- No guardar contraseñas ni secretos.
- No utilizar datos reales de pacientes en desarrollo o pruebas.
- No realizar eliminaciones físicas de información histórica.
- Mantener las reglas de negocio en la capa de servicios.
- Usar Huánuco como ubicación institucional.

La arquitectura y decisiones iniciales se encuentran en `docs/arquitectura/iteracion-01.md`.
