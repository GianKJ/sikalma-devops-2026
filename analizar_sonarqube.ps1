# Script de Escaneo SonarQube para Proyecto SIKALMA
param (
    [string]$Token = "",
    [string]$SonarHost = "http://localhost:9000",
    [string]$ProjectKey = "sikalma-backend"
)

Clear-Host
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "     SIKALMA - ANALISIS DE CALIDAD CON SONARQUBE" -ForegroundColor Yellow
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

if ([string]::IsNullOrWhiteSpace($Token)) {
    Write-Host "SonarQube exige obligatoriamente un TOKEN (no acepta contrasenas)." -ForegroundColor Yellow
    Write-Host "Copia el Token que acabas de generar en tu navegador." -ForegroundColor White
    Write-Host "Ejemplo de token: sqp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" -ForegroundColor DarkGray
    Write-Host ""
    $Token = Read-Host "Pega aqui tu Token de SonarQube"
}

if ([string]::IsNullOrWhiteSpace($Token)) {
    Write-Host "[ERROR] El Token no puede estar vacio." -ForegroundColor Red
    exit 1
}

$Token = $Token.Trim()

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Join-Path $root "backend\api"

Write-Host ""
Write-Host "[1/2] Compilando, ejecutando pruebas JUnit y generando reporte JaCoCo..." -ForegroundColor Cyan
Set-Location $backendDir

& .\mvnw.cmd clean test

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Las pruebas unitarias fallaron. Corrige los errores antes de escanear." -ForegroundColor Red
    Set-Location $root
    exit 1
}

Write-Host ""
Write-Host "[2/2] Enviando reporte y metricas a SonarQube ($SonarHost)..." -ForegroundColor Cyan

$sonarArgs = @(
    "sonar:sonar",
    "-Dsonar.projectKey=$ProjectKey",
    "-Dsonar.projectName=SIKALMA-Backend",
    "-Dsonar.host.url=$SonarHost",
    "-Dsonar.token=$Token",
    "-Dsonar.login=$Token",
    "-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
)

& .\mvnw.cmd @sonarArgs

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host " [EXITO] Analisis completado satisfactoriamente!" -ForegroundColor Green
    Write-Host " Revisa tu dashboard en: $SonarHost/dashboard?id=$ProjectKey" -ForegroundColor Yellow
    Write-Host "========================================================" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "[ERROR] No se pudo completar el analisis en SonarQube." -ForegroundColor Red
    Write-Host "Verifica que el Token sea exactamente el copiado de SonarQube." -ForegroundColor Yellow
}

Set-Location $root
