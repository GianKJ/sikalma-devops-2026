@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - ANALIZADOR FRONTEND (REACT/JS) CON SONARQUBE
echo ========================================================
echo.
set /p SONAR_TOKEN="Ingresa tu Token de SonarQube (o presiona ENTER si no usas token): "

if "%SONAR_TOKEN%"=="" (
    echo Ejecutando SonarScanner para Frontend...
    npx -y sonarqube-scanner -Dsonar.host.url=http://localhost:9000 -Dsonar.projectKey=sikalma-frontend -Dsonar.projectName="SIKALMA Frontend Web" -Dsonar.sources=src
) else (
    echo Ejecutando SonarScanner para Frontend con Token...
    npx -y sonarqube-scanner -Dsonar.host.url=http://localhost:9000 -Dsonar.projectKey=sikalma-frontend -Dsonar.projectName="SIKALMA Frontend Web" -Dsonar.sources=src -Dsonar.token=%SONAR_TOKEN%
)

echo.
echo ========================================================
echo   ¡ANALISIS FRONTEND COMPLETADO!
echo   Revisa tus resultados en: http://localhost:9000
echo ========================================================
pause
