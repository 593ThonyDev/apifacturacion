package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Iva;
import softech.apifacturacion.response.Respuesta;

public interface IvaService {

    Page<Iva> getAll(Pageable pageable);

    Respuesta save(Iva iva);

    Respuesta update(Iva iva);

    Respuesta changeStatus(Integer idIva, Status status);

}
