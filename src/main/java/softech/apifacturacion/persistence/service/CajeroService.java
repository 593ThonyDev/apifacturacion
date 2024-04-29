package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.model.Cajero;
import softech.apifacturacion.response.Respuesta;

public interface CajeroService {

    Respuesta save(Cajero cajero);

    Respuesta update(Cajero cajero);

    Respuesta getByIdentificacion(String identificacion);

}
