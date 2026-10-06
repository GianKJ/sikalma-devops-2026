@echo off
chcp 65001 > nul
echo ========================================================
echo   SIKALMA - INICIALIZADOR Y PRIMER PUSH A GITHUB
echo ========================================================
echo.
echo Repositorio objetivo: https://github.com/GianKJ/sikalma-devops-2026.git
echo.

cd /d "%~dp0"

echo [1/5] Inicializando Git en la raíz del proyecto...
git init

echo.
echo [2/5] Configurando el repositorio remoto origin...
git remote remove origin 2>nul
git remote add origin https://github.com/GianKJ/sikalma-devops-2026.git

echo.
echo [3/5] Agregando archivos del proyecto (respetando .gitignore)...
git add .

echo.
echo [4/5] Creando el primer commit en la rama 'main'...
git commit -m "feat: initial commit - Proyecto SIKALMA Oficial DevOps 2026"
git branch -M main
git push -u origin main

echo.
echo [5/5] Creando y subiendo la rama 'develop'...
git checkout -b develop
git push -u origin develop

echo.
echo ========================================================
echo   ¡PROYECTO SUBIDO EXITOSAMENTE A GITHUB!
echo   Ramas creadas: main y develop
echo   URL: https://github.com/GianKJ/sikalma-devops-2026
echo ========================================================
pause
