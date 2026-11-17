@echo off
chcp 65001 > nul
echo ========================================================
echo   CENTRO PSICOLOGICO SIKALMA - INICIAR SONARQUBE LOCAL
echo ========================================================
echo.

echo Verificando si Docker esta en ejecucion...
docker info > nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker Desktop no esta iniciado.
    echo Por favor abre Docker Desktop y espera a que este en verde.
    pause
    exit /b 1
)

echo Verificando contenedor sonarqube...
docker ps -a --format "{{.Names}}" | findstr /i "^sonarqube$" > nul
if %errorlevel% equ 0 (
    echo El contenedor 'sonarqube' ya existe. Iniciandolo...
    docker start sonarqube
) else (
    echo Descargando e iniciando SonarQube Community LTS...
    docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
)

echo.
echo ========================================================
echo   SonarQube se esta iniciando en segundo plano.
echo   Puede tardar entre 1 y 2 minutos en responder.
echo.
echo   Acceso Web: http://localhost:9000
echo   Usuario inicial:     admin
echo   Contrasena inicial:  admin
echo ========================================================
echo.
pause
