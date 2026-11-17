@echo off
title LANZADOR MAESTRO - SIKALMA
color 0F
echo =======================================================================
echo          SISTEMA INTEGRADO SIKALMA - LANZADOR AUTOMATICO
echo =======================================================================
echo.
echo [1/2] Levantando Backend Spring Boot (puerto 8080)...
start "SIKALMA [1] - Backend Spring Boot" cmd /c "cd /d ""%~dp0backend\api"" && title SIKALMA Backend && color 0A && .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local"

echo [2/2] Levantando Frontend React Vite...
start "SIKALMA [2] - Frontend React" cmd /c "cd /d ""%~dp0frontend\web"" && title SIKALMA Frontend && color 0B && npm run dev"

echo.
echo =======================================================================
echo  Servicios levantados en segundo plano:
echo  - Backend Web:  http://localhost:8080/login
echo                  (Usuario: admin  ^|  Clave: Admin123!)
echo  - Frontend App: http://localhost:5173  (o puerto mostrado en consola)
echo =======================================================================
echo.
echo Esperando inicializacion de servicios (8 segundos)...
timeout /t 8 /nobreak >nul

echo Abriendo navegador en las vistas del sistema...
start http://localhost:8080/login
start http://localhost:5173

echo.
echo =======================================================================
echo  LISTO. Manten las ventanas negras abiertas mientras uses el sistema.
echo  Para cerrar el sistema, simplemente cierra las 2 ventanas de consola.
echo =======================================================================
pause
