package softech.apifacturacion.persistence.service;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Establecimiento;
import softech.apifacturacion.persistence.model.dto.EstablecimientoDto;
import softech.apifacturacion.persistence.model.dto.EstablecimientoPageDto;
import softech.apifacturacion.response.Respuesta;

public interface EstablecimientoService {

    Respuesta save(String ruc, Establecimiento establecimiento, MultipartFile logo);

    Respuesta update(Establecimiento establecimiento);

    Respuesta updateLogo(Integer idEstablecimiento, MultipartFile logo);

    Respuesta changeStatus(Integer idEstablecimiento, Status status);

    List<EstablecimientoDto> getAllByRuc(String ruc);

    Page<EstablecimientoPageDto> getAll(Pageable pageable);

    Respuesta getById(Integer idEstablecimiento);

}
