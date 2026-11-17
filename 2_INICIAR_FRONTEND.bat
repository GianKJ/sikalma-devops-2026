@echo off
title SIKALMA - Frontend React (Vite SPA)
color 0B
cd /d "%~dp0frontend\web"
echo =======================================================================
echo          CENTRO PSICOLOGICO SIKALMA - INICIO DE FRONTEND
echo =======================================================================
echo  Tecnologia: React 18 + Vite
echo  Vistas:     Login demo, Admision de Pacientes/Usuarios, Agendar Citas
echo =======================================================================
echo.
echo Iniciando servidor de desarrollo Vite...
call npm run dev
pause
