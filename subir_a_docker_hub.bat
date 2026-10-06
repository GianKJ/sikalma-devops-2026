@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - CONSTRUIR Y SUBIR IMAGENES A DOCKER HUB
echo ========================================================
echo.
set /p DOCKER_USER="Ingresa tu nombre de usuario de Docker Hub: "
if "%DOCKER_USER%"=="" (
    echo Error: Debes ingresar tu usuario de Docker Hub.
    pause
    exit /b
)

echo.
echo [1/4] Iniciando sesión en Docker Hub...
docker login -u %DOCKER_USER%
if %errorlevel% neq 0 (
    echo Error en la autenticación con Docker Hub.
    pause
    exit /b
)

echo.
echo [2/4] Construyendo las imágenes del Backend y Frontend...
docker build -t %DOCKER_USER%/sikalma-backend:latest ./backend/api
docker build -t %DOCKER_USER%/sikalma-frontend:latest ./frontend/web

echo.
echo [3/4] Subiendo imagen de Backend (%DOCKER_USER%/sikalma-backend:latest)...
docker push %DOCKER_USER%/sikalma-backend:latest

echo.
echo [4/4] Subiendo imagen de Frontend (%DOCKER_USER%/sikalma-frontend:latest)...
docker push %DOCKER_USER%/sikalma-frontend:latest

echo.
echo ========================================================
echo   ¡IMAGENES PUBLICADAS EXITOSAMENTE EN DOCKER HUB!
echo   Backend:  https://hub.docker.com/r/%DOCKER_USER%/sikalma-backend
echo   Frontend: https://hub.docker.com/r/%DOCKER_USER%/sikalma-frontend
echo ========================================================
pause
