@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - DETENER ENTORNO DOCKER COMPOSE
echo ========================================================
echo.
cd /d "%~dp0"

docker compose down

echo.
echo ========================================================
echo   ¡CONTENEDORES DETENIDOS Y LIMPIOS!
echo ========================================================
pause
