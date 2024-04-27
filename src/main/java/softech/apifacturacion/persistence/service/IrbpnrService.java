package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Irbpnr;
import softech.apifacturacion.response.Respuesta;
public interface IrbpnrService {

    Page<Irbpnr> getAll(Pageable pageable);

    Respuesta save(Irbpnr Irbpnr);

    Respuesta update(Irbpnr Irbpnr);

    Respuesta changeStatus(Integer idIrbpnrInteger, Status status);

}
