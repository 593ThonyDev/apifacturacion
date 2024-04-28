package softech.apifacturacion.persistence.service;


import org.springframework.data.domain.*;
import softech.apifacturacion.persistence.model.Cliente;
import softech.apifacturacion.response.Respuesta;

public interface ClienteService {

    
    Page<Cliente> getAll(Pageable pageable);

    Respuesta save(Cliente cliente ,String ruc);

    Respuesta update(Cliente Cliente );

    Respuesta getbyid(Integer idCliente);

    Respuesta getbyruc(Integer idCliente , String ruc);
}