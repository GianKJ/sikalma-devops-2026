IF OBJECT_ID(N'dbo.especialidades', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.especialidades (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_especialidades PRIMARY KEY,
        nombre NVARCHAR(120) NOT NULL,
        descripcion NVARCHAR(500) NULL,
        activo BIT NOT NULL,
        fecha_creacion DATETIME2 NOT NULL,
        fecha_actualizacion DATETIME2 NOT NULL,
        CONSTRAINT uq_especialidades_nombre UNIQUE (nombre)
    )
END;

IF OBJECT_ID(N'dbo.servicios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.servicios (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_servicios PRIMARY KEY,
        nombre NVARCHAR(140) NOT NULL,
        descripcion NVARCHAR(1000) NOT NULL,
        modalidad VARCHAR(20) NOT NULL,
        duracion_minutos INT NOT NULL,
        publico_objetivo NVARCHAR(250) NOT NULL,
        activo BIT NOT NULL,
        fecha_creacion DATETIME2 NOT NULL,
        fecha_actualizacion DATETIME2 NOT NULL,
        CONSTRAINT uq_servicios_nombre UNIQUE (nombre),
        CONSTRAINT ck_servicios_duracion CHECK (duracion_minutos BETWEEN 15 AND 480),
        CONSTRAINT ck_servicios_modalidad CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'AMBAS'))
    )
END;

IF OBJECT_ID(N'dbo.psicologos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.psicologos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_psicologos PRIMARY KEY,
        tipo_documento VARCHAR(20) NOT NULL,
        numero_documento VARCHAR(20) NOT NULL,
        nombres NVARCHAR(100) NOT NULL,
        apellidos NVARCHAR(120) NOT NULL,
        colegiatura VARCHAR(30) NULL,
        telefono VARCHAR(20) NOT NULL,
        correo VARCHAR(160) NULL,
        descripcion_profesional NVARCHAR(1200) NULL,
        modalidad VARCHAR(20) NOT NULL,
        activo BIT NOT NULL,
        fecha_creacion DATETIME2 NOT NULL,
        fecha_actualizacion DATETIME2 NOT NULL,
        CONSTRAINT uq_psicologos_documento UNIQUE (numero_documento),
        CONSTRAINT ck_psicologos_modalidad CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'AMBAS'))
    )
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'ux_psicologos_correo' AND object_id = OBJECT_ID(N'dbo.psicologos'))
BEGIN
    CREATE UNIQUE INDEX ux_psicologos_correo ON dbo.psicologos(correo) WHERE correo IS NOT NULL
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'ux_psicologos_colegiatura' AND object_id = OBJECT_ID(N'dbo.psicologos'))
BEGIN
    CREATE UNIQUE INDEX ux_psicologos_colegiatura ON dbo.psicologos(colegiatura) WHERE colegiatura IS NOT NULL
END;

IF OBJECT_ID(N'dbo.pacientes', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.pacientes (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_pacientes PRIMARY KEY,
        tipo_documento VARCHAR(20) NOT NULL,
        numero_documento VARCHAR(20) NOT NULL,
        nombres NVARCHAR(100) NOT NULL,
        apellidos NVARCHAR(120) NOT NULL,
        fecha_nacimiento DATE NOT NULL,
        telefono VARCHAR(20) NOT NULL,
        correo VARCHAR(160) NULL,
        direccion NVARCHAR(250) NULL,
        contacto_emergencia NVARCHAR(180) NULL,
        apoderado NVARCHAR(180) NULL,
        observaciones_administrativas NVARCHAR(1000) NULL,
        consentimiento_registrado BIT NOT NULL,
        preferencias_privacidad NVARCHAR(500) NULL,
        activo BIT NOT NULL,
        fecha_creacion DATETIME2 NOT NULL,
        fecha_actualizacion DATETIME2 NOT NULL,
        CONSTRAINT uq_pacientes_documento UNIQUE (numero_documento)
    )
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'ux_pacientes_correo' AND object_id = OBJECT_ID(N'dbo.pacientes'))
BEGIN
    CREATE UNIQUE INDEX ux_pacientes_correo ON dbo.pacientes(correo) WHERE correo IS NOT NULL
END;

IF OBJECT_ID(N'dbo.psicologo_especialidad', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.psicologo_especialidad (
        psicologo_id BIGINT NOT NULL,
        especialidad_id BIGINT NOT NULL,
        CONSTRAINT pk_psicologo_especialidad PRIMARY KEY (psicologo_id, especialidad_id),
        CONSTRAINT fk_psicologo_especialidad_psicologo FOREIGN KEY (psicologo_id) REFERENCES dbo.psicologos(id),
        CONSTRAINT fk_psicologo_especialidad_especialidad FOREIGN KEY (especialidad_id) REFERENCES dbo.especialidades(id)
    )
END;

IF OBJECT_ID(N'dbo.psicologo_servicio', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.psicologo_servicio (
        psicologo_id BIGINT NOT NULL,
        servicio_id BIGINT NOT NULL,
        CONSTRAINT pk_psicologo_servicio PRIMARY KEY (psicologo_id, servicio_id),
        CONSTRAINT fk_psicologo_servicio_psicologo FOREIGN KEY (psicologo_id) REFERENCES dbo.psicologos(id),
        CONSTRAINT fk_psicologo_servicio_servicio FOREIGN KEY (servicio_id) REFERENCES dbo.servicios(id)
    )
END;
