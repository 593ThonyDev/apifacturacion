package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Irbpnr;
import softech.apifacturacion.persistence.repository.IrbpnrRepository;
import softech.apifacturacion.persistence.service.IrbpnrService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@Service
@RequiredArgsConstructor
public class IrbpnrServiceImpl implements IrbpnrService {

    @Autowired
    private final IrbpnrRepository repository;

    @Override
    public Page<Irbpnr> getAll(Pageable pageable) {
        Page<Irbpnr> pagina = repository.findAll(pageable);
        if (pagina.isEmpty()) {
            return null;
        }
        return pagina;
    }

    @Override
    public Respuesta save(Irbpnr Irbpnr) {
        if (Irbpnr.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (Irbpnr.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al Irbpnr")
                    .build();
        }
        if (Irbpnr.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        repository.save(Irbpnr);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Irbpnr Irbpnr) {

        if (Irbpnr.getIdIrbpnr() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar id valido")
                    .build();
        }
        if (Irbpnr.getCodigoPorcentaje().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un codigo de porcentaje")
                    .build();
        }
        if (Irbpnr.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre al Irbpnr")
                    .build();
        }
        if (Irbpnr.getTarifa() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una tarifa")
                    .build();
        }

        Optional<Irbpnr> optional = repository.findById(Irbpnr.getIdIrbpnr());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro")
                    .build();
        }

        repository.save(Irbpnr);
        return Respuesta.builder()
                .message("Registro actualizado correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

    @Override
    public Respuesta changeStatus(Integer idIrbpnr, Status status) {

        Optional<Irbpnr> optional = repository.findById(idIrbpnr);

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