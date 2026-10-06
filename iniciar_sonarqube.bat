@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - INICIADOR AUTOMATICO DE SONARQUBE CON DOCKER
echo ========================================================
echo.

echo [1/3] Verificando si el contenedor de SonarQube ya existe...
docker ps -a --filter "name=sonarqube-sikalma" --format "{{.Names}}" | findstr "sonarqube-sikalma" > nul
if %errorlevel% equ 0 (
    echo El contenedor 'sonarqube-sikalma' ya existe. Iniciandolo...
    docker start sonarqube-sikalma
) else (
    echo Creando y levantando nuevo contenedor de SonarQube Community...
    docker run -d --name sonarqube-sikalma -p 9000:9000 sonarqube:lts-community
)

echo.
echo [2/3] Esperando a que SonarQube inicialice (suele tardar aprox. 40-60 segundos)...
echo Puedes monitorear los logs abriendo otra terminal con: docker logs -f sonarqube-sikalma
echo.

:CHECK_LOOP
timeout /t 5 /nobreak > nul
curl -s http://localhost:9000/api/system/status | findstr "UP" > nul
if %errorlevel% neq 0 (
    echo Esperando a que SonarQube este UP...
    goto CHECK_LOOP
)

echo.
echo ========================================================
echo   ¡SONARQUBE ESTA LISTO Y OPERATIVO!
echo ========================================================
echo   URL: http://localhost:9000
echo   Usuario inicial: admin
echo   Clave inicial:   admin
echo.
echo Ahora se abrira SonarQube en tu navegador.
echo 1. Cambia la contrasena inicial (por ej: Sikalma2026*)
echo 2. Clic en 'Create Project' -> 'Manually' -> Nombre: sikalma-backend
echo 3. Genera un Token (Project Analysis Token)
echo.
start http://localhost:9000
pause
