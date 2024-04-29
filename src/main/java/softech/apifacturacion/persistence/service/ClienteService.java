package softech.apifacturacion.persistence.service;

import java.util.List;

import org.springframework.data.domain.*;
import softech.apifacturacion.persistence.model.Cliente;
import softech.apifacturacion.persistence.model.dto.ClienteDto;
import softech.apifacturacion.persistence.model.dto.ClienteRucDto;
import softech.apifacturacion.response.Respuesta;

public interface ClienteService {

    Page<ClienteDto> getAll(Pageable pageable);

    Page<ClienteRucDto> getAllByRuc(String ruc,Pageable pageable);

    Respuesta save(Cliente cliente, String ruc);

    Respuesta update(Cliente Cliente);

    Respuesta getbyid(Integer idCliente);

    Respuesta getbyruc(Integer idCliente, String ruc);

    List<ClienteRucDto> findByRucAndIdentificacion(String ruc, String identificacion);

    List<ClienteDto> findByIdentificacion(String identificacion);

}