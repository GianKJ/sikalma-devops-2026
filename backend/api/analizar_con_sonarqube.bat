@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - ANALIZADOR DE CODIGO CON SONARQUBE + JACOCO
echo ========================================================
echo.
set /p SONAR_TOKEN="Ingresa tu Token de SonarQube (o presiona ENTER para usar admin/admin123): "

if "%SONAR_TOKEN%"=="" (
    echo.
    echo Ejecutando analisis con usuario admin y password admin123...
    .\mvnw.cmd clean verify sonar:sonar ^
      -Dsonar.host.url=http://localhost:9000 ^
      -Dsonar.login=admin ^
      -Dsonar.password=admin123
) else (
    echo.
    echo Ejecutando analisis con Token proporcionado...
    .\mvnw.cmd clean verify sonar:sonar ^
      -Dsonar.host.url=http://localhost:9000 ^
      -Dsonar.token=%SONAR_TOKEN%
)

echo.
echo ========================================================
echo   ¡ANALISIS COMPLETADO!
echo   Revisa tus resultados en: http://localhost:9000
echo ========================================================
pause
