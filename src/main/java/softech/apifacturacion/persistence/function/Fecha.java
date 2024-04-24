package softech.apifacturacion.persistence.function;

import java.time.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
public class Fecha {

    /**
     * Obtiene la fecha y hora actual en la zona horaria de America/Guayaquil.
     *
     * @return La fecha y hora actual en la zona horaria de America/Guayaquil.
     */
    public ZonedDateTime fechaCreacion() {
        return ZonedDateTime.now(ZoneId.of("America/Guayaquil"));
    }

    /**
     * Agrega el número especificado de meses a la fecha dada.
     *
     * @param fecha La fecha a la que se le agregarán los meses.
     * @param meses El número de meses a agregar.
     * @return La fecha con los meses agregados.
     */
    public ZonedDateTime addMeses(ZonedDateTime fecha, int meses) {
        return fecha.plusMonths(meses);
    }

    /**
     * Verifica si una fecha no ha pasado en relación con la fecha actual en la zona
     * horaria de America/Guayaquil.
     *
     * @param fecha La fecha a comparar.
     * @return true si la fecha no ha pasado, false si la fecha ha pasado.
     */
    public boolean fechaNoHaPasado(ZonedDateTime fecha) {
        // Obtiene la fecha actual en la zona horaria de America/Guayaquil
        ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Guayaquil"));
        // Compara la fecha dada con la fecha actual, devuelve true si la fecha no es
        // anterior a la actual
        return !fecha.isBefore(ahora);
    }

}
