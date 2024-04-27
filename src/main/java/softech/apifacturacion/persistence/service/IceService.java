package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Ice;
import softech.apifacturacion.response.Respuesta;

public interface IceService {

    Page<Ice> getAll(Pageable pageable);

    Respuesta save(Ice ice);

    Respuesta update(Ice ice);

    Respuesta changeStatus(Integer idIceInteger, Status status);

}