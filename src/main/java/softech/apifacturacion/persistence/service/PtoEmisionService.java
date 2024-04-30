package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.PtoEmision;
import softech.apifacturacion.response.Respuesta;

public interface PtoEmisionService {

    Respuesta save(String ruc, PtoEmision ptoEmision);

    Respuesta changeStatus(String ruc, Integer idPuntoEmision, Status status);
}
