/*
  Preparación inicial de SQL Server para SIKALMA.
  Ejecutar una sola vez con una cuenta administradora.
  Reemplazar la contraseña de ejemplo antes de ejecutar.
*/

USE master;
GO

IF DB_ID(N'sikalma_db') IS NULL
BEGIN
    CREATE DATABASE sikalma_db;
END;
GO

IF SUSER_ID(N'sikalma_user') IS NULL
BEGIN
    CREATE LOGIN sikalma_user
        WITH PASSWORD = 'CAMBIAR_POR_UNA_CLAVE_SEGURA',
        CHECK_POLICY = ON,
        CHECK_EXPIRATION = OFF;
END;
GO

USE sikalma_db;
GO

IF DATABASE_PRINCIPAL_ID(N'sikalma_user') IS NULL
BEGIN
    CREATE USER sikalma_user FOR LOGIN sikalma_user;
END;
GO

ALTER ROLE db_datareader ADD MEMBER sikalma_user;
ALTER ROLE db_datawriter ADD MEMBER sikalma_user;
ALTER ROLE db_ddladmin ADD MEMBER sikalma_user;
GO
