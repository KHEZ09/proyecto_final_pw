# Triggers de reportes

Las definiciones estan en `src/main/resources/database/triggers.sql`.
`config/TriggerInitializer.java` las ejecuta al arrancar Spring Boot, despues
de preparar las tablas JPA. No se necesitan dependencias adicionales.

| Trigger | Evento | Funcion |
| --- | --- | --- |
| `trg_reportes_inicializar` | Antes de INSERT | Completa `creado_en`, `actualizado_en` y el estado `RECIBIDO` cuando faltan. Conserva los valores explicitos. |
| `trg_reportes_actualizar_fecha` | Antes de UPDATE | Actualiza `actualizado_en` y conserva la fecha original `creado_en`. |

Los triggers se ejecutan tanto para cambios enviados desde Java como para
INSERT y UPDATE ejecutados directamente desde pgAdmin. No modifican los
permisos de la aplicacion ni eliminan reportes.

## Verlos en pgAdmin

Actualizar el arbol y abrir:

`reparaweb > Schemas > public > Tables > reportes > Triggers`

Las funciones asociadas se encuentran en `public > Trigger Functions`.
Tambien se pueden consultar con:

```sql
SELECT tgname, pg_get_triggerdef(oid)
FROM pg_trigger
WHERE tgrelid = 'public.reportes'::regclass
  AND NOT tgisinternal;
```

## Instalacion manual

Con las tablas ya creadas, abrir el archivo `triggers.sql` completo en Query
Tool y ejecutarlo conectado a la base `reparaweb`. Se puede repetir: utiliza
CREATE OR REPLACE para no duplicar funciones ni triggers.

## Verificacion

Ejecutar `mvnw.cmd test` con PostgreSQL disponible y la conexion local
configurada. `ReporteTriggersTests` comprueba valores iniciales, conservacion
de estados explicitos, fechas de actualizacion e instalacion repetible.
Los registros insertados por esas pruebas se revierten mediante transacciones.
