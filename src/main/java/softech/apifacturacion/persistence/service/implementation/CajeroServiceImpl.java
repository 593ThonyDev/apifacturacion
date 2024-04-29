package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.Cajero;
import softech.apifacturacion.persistence.repository.CajeroRepository;
import softech.apifacturacion.persistence.service.CajeroService;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class CajeroServiceImpl implements CajeroService {

    @Autowired
    private final CajeroRepository repository;

    @Override
    public Respuesta save(Cajero cajero) {

        if (cajero.getNombres().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar los nombres del cajero")
                    .build();
        }
        if (cajero.getApellidos().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar los apellidos del cajero")
                    .build();
        }
        if (cajero.getEmail().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el email del cajero")
                    .build();
        }

        Optional<Cajero> optional = repository.findByIdentificacion(cajero.getIdentificacion());
        if (optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("El email del cajero ya se encentra ocupado")
                    .build();
        }

        cajero.setCreated(new Fecha().fechaCreacion());

        repository.save(cajero);

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Cajero cajero) {
        if (cajero.getNombres().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar los nombres del cajero")
                    .build();
        }
        if (cajero.getApellidos().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar los apellidos del cajero")
                    .build();
        }

        Optional<Cajero> optional = repository.findByIdentificacion(cajero.getIdentificacion());
        if (optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("El cajero ya se encuentra registrado con esa identificacion")
                    .build();
        }

        cajero.setEmail(optional.get().getEmail());
        cajero.setCreated(optional.get().getCreated());

        repository.save(cajero);

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();
    }

    @Override
    public Respuesta getByIdentificacion(String identificacion) {

        Optional<Cajero> optional = repository.findByIdentificacion(identificacion);
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Registro no encontrado")
                    .build();
        }
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .content(new Object[] { optional.get() })
                .build();

    }

}
