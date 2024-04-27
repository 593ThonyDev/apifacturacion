package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Ice;
import softech.apifacturacion.persistence.repository.IceRepository;
import softech.apifacturacion.persistence.service.IceService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@Service
@RequiredArgsConstructor
public class IceServiceImpl implements IceService {

    @Autowired
    private final IceRepository repository;

    @Override
    public Page<Ice> getAll(Pageable pageable) {
        Page<Ice> pagina = repository.findAll(pageable);
        if (pagina.isEmpty()) {
            return null;
        }
        return pagina;
    }

    @Override
    public Respuesta save(Ice ice) {
        if (ice.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (ice.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al iva")
                    .build();
        }
        if (ice.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        repository.save(ice);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Ice ice) {

        if (ice.getIdIce() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar id valido")
                    .build();
        }
        if (ice.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (ice.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al iva")
                    .build();
        }
        if (ice.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        Optional<Ice> optional = repository.findById(ice.getIdIce());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro")
                    .build();
        }

        repository.save(ice);
        return Respuesta.builder()
                .message("Registro actualizado correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

    @Override
    public Respuesta changeStatus(Integer idIce, Status status) {

        Optional<Ice> optional = repository.findById(idIce);

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

