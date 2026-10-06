@echo off
title SIKALMA - Suite Oficial de Pruebas de Software
cls
powershell -ExecutionPolicy Bypass -File "%~dp0ejecutar_pruebas.ps1"
pause
