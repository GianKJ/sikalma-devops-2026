-- =========================================================
-- SIKALMA - Script de Inicializacion de Base de Datos (init.sql)
-- Compatible con PostgreSQL y SQL ANSI estándar
-- =========================================================

CREATE TABLE IF NOT EXISTS especialidades (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS servicios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(140) NOT NULL UNIQUE,
    descripcion VARCHAR(1000) NOT NULL,
    modalidad VARCHAR(20) NOT NULL CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'AMBAS')),
    duracion_minutos INT NOT NULL CHECK (duracion_minutos BETWEEN 15 AND 480),
    publico_objetivo VARCHAR(250) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS psicologos (
    id BIGSERIAL PRIMARY KEY,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    colegiatura VARCHAR(30) UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(160) UNIQUE,
    descripcion_profesional VARCHAR(1200),
    modalidad VARCHAR(20) NOT NULL CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'AMBAS')),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(160) UNIQUE,
    direccion VARCHAR(250),
    contacto_emergencia VARCHAR(180),
    apoderado VARCHAR(180),
    observaciones_administrativas VARCHAR(1000),
    consentimiento_registrado BOOLEAN NOT NULL DEFAULT TRUE,
    preferencias_privacidad VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS psicologo_especialidad (
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id) ON DELETE CASCADE,
    especialidad_id BIGINT NOT NULL REFERENCES especialidades(id) ON DELETE CASCADE,
    PRIMARY KEY (psicologo_id, especialidad_id)
);

CREATE TABLE IF NOT EXISTS psicologo_servicio (
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id) ON DELETE CASCADE,
    servicio_id BIGINT NOT NULL REFERENCES servicios(id) ON DELETE CASCADE,
    PRIMARY KEY (psicologo_id, servicio_id)
);
