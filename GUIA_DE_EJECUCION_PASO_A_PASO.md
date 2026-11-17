# Guía de Ejecución de SIKALMA v3.0.0 (Localhost, Docker, Cypress E2E y VS Code)

**Curso:** Pruebas y Calidad de Software (`NRC 30180`) — **Docente:** Dr. Maglioni Arana Caparachin  
**Equipo:** Cavero Ayzana, Aldheyr Mitsui (`62060023`) · Ramirez Soto, Juan Diego (`72552392`) · Sanchez Vilchez, Giancarlo Robert (`73175492`)

---

## 1. Cuentas de Acceso (Pre-cargadas en modo `local` y `postgres`)

| Usuario | Contraseña | Rol | Ruta inicial | Alcance |
|---|---|---|---|---|
| `admin` | `Admin123!` | Administrador | `/admin/inicio` | Métricas, gráficos, Citas, Usuarios/Roles, Pacientes, Psicólogos, Especialidades y Servicios |
| `recepcion` | `Recepcion123!` | Recepcionista | `/recepcion/inicio` | Panel de recepción, agenda de Citas anti-cruces, Pacientes y comprobantes (`403` en Usuarios y notas clínicas) |
| `maryory` | `Psico123!` | Psicóloga | `/profesional/inicio` | Agenda clínica aislada con notas de evolución (`403` en otros profesionales/admin) |
| `antony` | `Fisio123!` | Fisioterapeuta | `/profesional/inicio` | Agenda de fisioterapia aislada (`403` en otros profesionales/admin) |

---

## 2. Forma 1: Ejecutar por Comandos (PowerShell / Terminal)

### Levantar el sistema completo en `http://localhost:8080`
```powershell
cd "D:\Proyecto sikalma\backend\api"
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```
*O directamente con el JAR compilado:*
```powershell
java -jar "D:\Proyecto sikalma\backend\api\target\sikalma-3.0.0.jar" --spring.profiles.active=local
```

### Levantar el Frontend React SPA en `http://localhost:4200`
```powershell
cd "D:\Proyecto sikalma\frontend\web"
npm run dev
```

### Ejecutar todas las pruebas automatizadas (en verde)
```powershell
# 1. Backend JUnit 5 + MockMvc + JaCoCo (34/34 aprobadas, 98.6% instrucciones)
cd "D:\Proyecto sikalma\backend\api"
.\mvnw.cmd clean verify

# 2. Frontend Unitarias Vitest (10/10 aprobadas, 91.89% cobertura)
cd "D:\Proyecto sikalma\frontend\web"
npm test -- --run

# 3. Cypress E2E sobre localhost:8080 (con backend 8080 encendido)
cd "D:\Proyecto sikalma\frontend\web"
npx cypress run --config-file cypress.sikalma8080.config.js

# 4. Cypress E2E sobre localhost:4200 (con frontend 4200 encendido)
cd "D:\Proyecto sikalma\frontend\web"
npx cypress run
```

---

## 3. Forma 2: Ejecutar en Visual Studio Code (Pruebas en Verde)

1. Abre la carpeta raíz `D:\Proyecto sikalma` en Visual Studio Code (`.vscode/settings.json` y `.vscode/tasks.json` ya están configurados).
2. Abre la pestaña **Testing** (ícono de matraz en la barra izquierda) para ver y ejecutar los tests Java (`backend/api`) y Vitest (`frontend/web`) en verde.
3. Usa `Ctrl+Shift+P` -> **Tasks: Run Test Task** y elige:
   - `SIKALMA: Ejecutar TODAS las Pruebas Unitarias y de Integracion`
   - `SIKALMA: E2E Cypress Sistema 8080`
   - `SIKALMA: E2E Cypress Frontend 4200`

---

## 4. Docker Compose y SonarQube
```powershell
cd "D:\Proyecto sikalma"
docker compose up --build -d
```
Para re-escanear SonarQube (`http://localhost:9000`) con un token personal válido (`sqp_...`):
```powershell
cd "D:\Proyecto sikalma\backend\api"
.\mvnw.cmd sonar:sonar -Dsonar.projectKey=sikalma-backend -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<TU_TOKEN>
```
