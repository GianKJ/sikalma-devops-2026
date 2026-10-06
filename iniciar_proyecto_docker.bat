@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - LEVANTAR TODO EL ENTORNO CON DOCKER COMPOSE
echo ========================================================
echo.
echo Servicios a iniciar:
echo  1. Base de Datos (PostgreSQL 16 en puerto 5432)
echo  2. Backend API (Spring Boot 21 en puerto 3000)
echo  3. Frontend Web (React 18 en puerto 4200)
echo.
cd /d "%~dp0"

echo [1/2] Construyendo imagenes e iniciando contenedores...
docker compose up -d --build

echo.
echo [2/2] Verificando estado de los servicios...
docker compose ps

echo.
echo ========================================================
echo   ¡ENTORNO SIKALMA EN DOCKER LEVANTADO CON EXITO!
echo ========================================================
echo   Frontend Web: http://localhost:4200
echo   Backend API:  http://localhost:3000
echo   PostgreSQL:   localhost:5432 (BD: sikalma_db)
echo ========================================================
echo.
echo Para ver los logs en tiempo real, ejecuta: docker compose logs -f
echo Para detener los contenedores, ejecuta: docker compose down
echo.
pause
