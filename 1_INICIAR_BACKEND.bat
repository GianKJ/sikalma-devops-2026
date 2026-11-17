@echo off
title SIKALMA - Backend Spring Boot (H2 Local)
color 0A
cd /d "%~dp0backend\api"
echo =======================================================================
echo          CENTRO PSICOLOGICO SIKALMA - INICIO DE BACKEND
echo =======================================================================
echo  Perfil activo: LOCAL (Base de datos H2 en memoria - Sin dependencias)
echo  URL Acceso:    http://localhost:8080/login
echo  Usuario:       admin
echo  Clave:         Admin123!
echo =======================================================================
echo.
echo Compilando y ejecutando servidor Tomcat...
call .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
pause
