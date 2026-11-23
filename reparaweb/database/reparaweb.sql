-- Crear la base de datos reparaweb en PostgreSQL y ejecutar este script.
CREATE TABLE IF NOT EXISTS usuarios (id BIGSERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, nombre VARCHAR(80) NOT NULL, rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMINISTRADOR','TECNICO','USUARIO')));
CREATE TABLE IF NOT EXISTS reportes (id BIGSERIAL PRIMARY KEY, codigo VARCHAR(20) NOT NULL UNIQUE, titulo VARCHAR(120) NOT NULL, descripcion VARCHAR(1000) NOT NULL, categoria VARCHAR(40) NOT NULL, ubicacion VARCHAR(120) NOT NULL, urgencia VARCHAR(15) NOT NULL, estado VARCHAR(20) NOT NULL, foto_url VARCHAR(500), creado_por VARCHAR(50) NOT NULL, responsable VARCHAR(50), solucion VARCHAR(500), materiales VARCHAR(500), programado_para TIMESTAMP, creado_en TIMESTAMP NOT NULL, actualizado_en TIMESTAMP);
-- La aplicación crea y actualiza las tablas automáticamente con spring.jpa.hibernate.ddl-auto=update.
-- Los triggers estan en src/main/resources/database/triggers.sql.
-- TriggerInitializer los instala automaticamente al arrancar, despues de JPA.
-- Para instalarlos manualmente, ejecutar ese archivo completo en pgAdmin.
