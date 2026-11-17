@echo off
chcp 65001 > nul
echo ========================================================
echo      SIKALMA - ANALISIS DE CALIDAD CON SONARQUBE
echo ========================================================
echo.
echo SonarQube exige un TOKEN de autenticacion (empieza con sqp_ o similar).
echo Generalo en la pantalla que tienes abierta en el navegador.
echo (O presiona ENTER para usar tu token activo automaticamente).
echo.

set "DEFAULT_TOKEN=sqp_a3679db36011a1139ab215cf0f9994212a54a1e2"
set /p TOKEN="Pega aqui tu Token de SonarQube [ENTER = activo]: "
if "%TOKEN%"=="" set "TOKEN=%DEFAULT_TOKEN%"
set "TOKEN=%TOKEN: =%"

echo.
echo [1/2] Compilando, ejecutando pruebas JUnit y generando reporte JaCoCo...
cd /d "%~dp0backend\api"

call mvnw.cmd clean test
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Las pruebas fallaron. Corrige los errores antes de continuar.
    pause
    exit /b 1
)

echo.
echo [2/2] Enviando metricas y cobertura a SonarQube (http://localhost:9000)...
call mvnw.cmd sonar:sonar -Dsonar.projectKey=sikalma-backend -Dsonar.projectName="SIKALMA-Backend" -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%TOKEN% -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

if %errorlevel% equ 0 (
    echo.
    echo ========================================================
    echo  [EXITO] Analisis completado con exito!
    echo  Abre tu dashboard en:
    echo  http://localhost:9000/dashboard?id=sikalma-backend
    echo ========================================================
) else (
    echo.
    echo [ERROR] Hubo un problema al autenticar en SonarQube.
    echo Verifica si copiaste el token completo sin espacios.
)

pause
