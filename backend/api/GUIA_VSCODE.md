# Guía para abrir y ejecutar SIKALMA en Visual Studio Code

## 1. Contenido del proyecto

El ZIP contiene el código fuente completo de SIKALMA, Maven Wrapper, configuración de Visual
Studio Code, pruebas, vistas Thymeleaf, recursos gráficos y scripts de SQL Server. No contiene
contraseñas reales, datos clínicos ni archivos temporales de compilación.

## 2. Programas necesarios

Instala estos programas antes de comenzar:

- Visual Studio Code.
- JDK 21 de Java.
- SQL Server 2022 o una versión compatible.
- SQL Server Management Studio, recomendado para administrar la base.

No necesitas instalar Maven. El proyecto incluye `mvnw.cmd` y descargará la versión necesaria.

## 3. Descomprimir y abrir

1. Descomprime `sikalma.zip` en una carpeta de trabajo.
2. Abre Visual Studio Code.
3. Selecciona **Archivo > Abrir carpeta**.
4. Selecciona la carpeta `sikalma` que contiene `pom.xml`.
5. Si VS Code pregunta si confías en los autores, confirma solamente si el ZIP proviene de esta
   entrega.
6. Espera a que Java y Maven terminen de importar el proyecto.

VS Code recomendará estas extensiones:

- Extension Pack for Java.
- Spring Boot Extension Pack.
- Maven for Java.
- XML.

Acepta la instalación de las extensiones recomendadas si todavía no las tienes.

## 4. Preparar SQL Server

En la computadora usada durante el desarrollo ya existen:

- servidor: `localhost`;
- puerto: `1433`;
- base: `sikalma_db`;
- usuario SQL: `sikalma_user`.

Si utilizas esa misma instalación, no vuelvas a crear la base. Si trabajas en otra computadora,
abre `docs/sqlserver/00-crear-base-y-usuario.sql` en SQL Server Management Studio, reemplaza la
contraseña de ejemplo y ejecútalo una sola vez con una cuenta administradora.

SQL Server debe aceptar conexiones TCP/IP por el puerto 1433 y autenticación de SQL Server. El
script de la aplicación creará automáticamente las tablas faltantes al primer inicio.

## 5. Crear la configuración local

Abre una terminal PowerShell dentro de VS Code y ejecuta:

```powershell
Copy-Item .env.example .env
```

Abre el nuevo archivo `.env` y reemplaza solamente los valores de contraseña:

```dotenv
SIKALMA_DB_HOST=localhost
SIKALMA_DB_PORT=1433
SIKALMA_DB_NAME=sikalma_db
SIKALMA_DB_USER=sikalma_user
SIKALMA_DB_PASSWORD=TU_CLAVE_DE_SQL_SERVER
SIKALMA_DB_TRUST_CERTIFICATE=true
SESSION_COOKIE_SECURE=false
SPRING_SECURITY_USER_NAME=user
SPRING_SECURITY_USER_PASSWORD=TU_CLAVE_PARA_INGRESAR_A_SIKALMA
SPRING_SECURITY_USER_ROLES=ADMINISTRADOR
```

El archivo `.env` está excluido por `.gitignore`. No lo envíes, publiques ni agregues al ZIP.

## 6. Ejecutar desde Visual Studio Code

1. Abre la sección **Ejecutar y depurar** con `Ctrl+Shift+D`.
2. Selecciona **SIKALMA Desarrollo**.
3. Presiona el botón verde o la tecla `F5`.
4. Espera el mensaje `Started SikalmaApplication` en la consola.
5. Abre `http://localhost:8080` en el navegador.
6. Para entrar al panel, abre `http://localhost:8080/login` y utiliza el usuario y la contraseña
   definidos en `.env`.

Rutas útiles:

- Sitio público: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Panel administrativo: `http://localhost:8080/admin/inicio`
- Pacientes: `http://localhost:8080/admin/pacientes`
- Psicólogos: `http://localhost:8080/admin/psicologos`
- Especialidades: `http://localhost:8080/admin/especialidades`
- Servicios: `http://localhost:8080/admin/servicios`

Para detener la aplicación, usa el botón rojo de VS Code.

## 7. Ejecutar pruebas

En la terminal integrada ejecuta:

```powershell
.\mvnw.cmd test
```

Las pruebas utilizan H2 en memoria y no modifican `sikalma_db`.

## 8. Ejecutar desde la terminal

Si no deseas usar `F5`, define las variables en la terminal actual:

```powershell
$env:SIKALMA_DB_PASSWORD="TU_CLAVE_DE_SQL_SERVER"
$env:SPRING_SECURITY_USER_NAME="user"
$env:SPRING_SECURITY_USER_PASSWORD="TU_CLAVE_PARA_INGRESAR_A_SIKALMA"
.\mvnw.cmd spring-boot:run
```

Detén el servidor con `Ctrl+C`.

## 9. Problemas comunes

### No se puede conectar a SQL Server

Comprueba que el servicio de SQL Server esté iniciado, que TCP/IP esté habilitado, que el puerto
sea 1433 y que el usuario `sikalma_user` pueda conectarse a `sikalma_db`.

### El puerto 8080 está ocupado

Ejecuta temporalmente en otro puerto:

```powershell
$env:PORT="8081"
.\mvnw.cmd spring-boot:run
```

### Usuario o contraseña incorrectos

Verifica `SPRING_SECURITY_USER_NAME` y `SPRING_SECURITY_USER_PASSWORD` dentro de `.env`. Después
detén y vuelve a iniciar la aplicación para aplicar los cambios.

### VS Code no reconoce Java

Comprueba en PowerShell:

```powershell
java -version
```

Debe mostrar Java 21. Después ejecuta en VS Code **Java: Clean Java Language Server Workspace** y
vuelve a abrir la carpeta.

## 10. Reglas de seguridad

- Utiliza únicamente datos ficticios durante desarrollo y demostraciones.
- No publiques `.env` ni contraseñas.
- No elimines registros históricos directamente en SQL Server.
- En producción usa un certificado TLS válido y cambia `SIKALMA_DB_TRUST_CERTIFICATE` a `false`.
- Cambia las credenciales temporales antes de publicar la aplicación.
