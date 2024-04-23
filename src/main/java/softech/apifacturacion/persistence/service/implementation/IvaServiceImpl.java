package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Iva;
import softech.apifacturacion.persistence.repository.IvaRepository;
import softech.apifacturacion.persistence.service.IvaService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@Service
@RequiredArgsConstructor
public class IvaServiceImpl implements IvaService {

    @Autowired
    private final IvaRepository repository;

    @Override
    public Page<Iva> getAll(Pageable pageable) {
        Page<Iva> pagina = repository.findAll(pageable);
        if (pagina.isEmpty()) {
            return null;
        }
        return pagina;
    }

    @Override
    public Respuesta save(Iva iva) {
        if (iva.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (iva.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al iva")
                    .build();
        }
        if (iva.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        repository.save(iva);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Iva iva) {

        if (iva.getIdIva() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar id valido")
                    .build();
        }
        if (iva.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (iva.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al iva")
                    .build();
        }
        if (iva.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        Optional<Iva> optional = repository.findById(iva.getIdIva());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro")
                    .build();
        }

        repository.save(iva);
        return Respuesta.builder()
                .message("Registro actualizado correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

    @Override
    public Respuesta changeStatus(Integer idIva, Status status) {

        Optional<Iva> optional = repository.findById(idIva);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro")
                    .build();
        }

        if (status == Status.OFFLINE) {
            optional.get().setStatus(Status.OFFLINE);
        } else {
            optional.get().setStatus(Status.ONLINE);
        }

        repository.save(optional.get());
        
        return Respuesta.builder()
                .message("Registro actualizado correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

}
