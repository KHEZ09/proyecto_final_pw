-- Se ejecuta despues de que Hibernate prepare public.reportes.
-- Tambien puede ejecutarse completo desde Query Tool de pgAdmin.
-- CREATE OR REPLACE permite repetir la instalacion sin duplicar triggers.

CREATE OR REPLACE FUNCTION public.reparaweb_inicializar_reporte()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.creado_en := COALESCE(NEW.creado_en, LOCALTIMESTAMP);
    NEW.actualizado_en := COALESCE(NEW.actualizado_en, NEW.creado_en);

    IF NEW.estado IS NULL OR BTRIM(NEW.estado) = '' THEN
        NEW.estado := 'RECIBIDO';
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER trg_reportes_inicializar
BEFORE INSERT ON public.reportes
FOR EACH ROW
EXECUTE FUNCTION public.reparaweb_inicializar_reporte();

CREATE OR REPLACE FUNCTION public.reparaweb_actualizar_reporte()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    -- La fecha original no cambia al editar o resolver un reporte.
    NEW.creado_en := OLD.creado_en;
    NEW.actualizado_en := statement_timestamp()::timestamp;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER trg_reportes_actualizar_fecha
BEFORE UPDATE ON public.reportes
FOR EACH ROW
EXECUTE FUNCTION public.reparaweb_actualizar_reporte();
