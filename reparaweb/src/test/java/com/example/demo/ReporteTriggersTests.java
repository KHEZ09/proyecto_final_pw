package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReporteTriggersTests {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    @Qualifier("instalarTriggers")
    private CommandLineRunner instalarTriggers;

    private Map<String, Object> insertar(String estado) {
        String codigo = "TEST-" + UUID.randomUUID().toString().substring(0, 12);
        return jdbc.queryForMap("""
                INSERT INTO public.reportes
                    (codigo, titulo, descripcion, categoria, ubicacion, urgencia, creado_por, estado)
                VALUES (?, 'Prueba de trigger', 'Registro temporal', 'Equipos', 'Pruebas', 'BAJA', 'usuario1', ?)
                RETURNING id, estado, creado_en, actualizado_en
                """, codigo, estado);
    }

    @Test
    void insertarSinFechasNiEstadoAsignaValoresIniciales() {
        var reporte = insertar(null);
        assertEquals("RECIBIDO", reporte.get("estado"));
        assertNotNull(reporte.get("creado_en"));
        assertEquals(reporte.get("creado_en"), reporte.get("actualizado_en"));
    }

    @Test
    void estadoVacioRecibePredeterminadoYEstadoExplicitoSeConserva() {
        assertEquals("RECIBIDO", insertar("   ").get("estado"));
        assertEquals("EN_PROCESO", insertar("EN_PROCESO").get("estado"));
    }

    @Test
    void actualizarRenuevaFechaYConservaFechaOriginal() {
        var reporte = insertar("RECIBIDO");
        var fechaFalsa = Timestamp.valueOf("2000-01-01 00:00:00");
        var actualizado = jdbc.queryForMap("""
                UPDATE public.reportes
                SET estado = 'RESUELTO', creado_en = ?, actualizado_en = ?
                WHERE id = ?
                RETURNING estado, creado_en, actualizado_en
                """, fechaFalsa, fechaFalsa, reporte.get("id"));
        assertEquals("RESUELTO", actualizado.get("estado"));
        assertEquals(reporte.get("creado_en"), actualizado.get("creado_en"));
        assertNotEquals(fechaFalsa, actualizado.get("actualizado_en"));
    }

    @Test
    void instalacionSePuedeRepetirSinDuplicados() throws Exception {
        instalarTriggers.run();
        assertEquals(2, jdbc.queryForObject("""
                SELECT COUNT(*) FROM pg_trigger
                WHERE tgrelid = 'public.reportes'::regclass
                  AND tgname IN ('trg_reportes_inicializar', 'trg_reportes_actualizar_fecha')
                  AND NOT tgisinternal
                """, Integer.class));
    }
}
