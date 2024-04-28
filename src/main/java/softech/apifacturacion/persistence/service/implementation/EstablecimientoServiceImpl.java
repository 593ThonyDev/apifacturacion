package softech.apifacturacion.persistence.service.implementation;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.EstablecimientoService;
import softech.apifacturacion.response.*;
import softech.apifacturacion.upload.UploadImage;

@Service
@RequiredArgsConstructor
public class EstablecimientoServiceImpl implements EstablecimientoService {

    @Autowired
    private final EstablecimientoRepository repository;

    @Autowired
    private final EmisorRepossitory emisorRepository;

    @Autowired
    private UploadImage upload;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Respuesta save(String ruc, Establecimiento establecimiento, MultipartFile logo) {

        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("El ruc indicado no existe")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getCodigo().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el codigo del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getWeb().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue la pagina web de su empresa")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getNombreComercial().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre comercial de si establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getDireccion().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue la direccion del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getEmail().isEmpty()) {
            return Respuesta.builder()
                    .message("Agergue el email del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (logo.isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el logo de su establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        establecimiento.setFkEmisor(optional.get());
        establecimiento.setStatus(Status.ONLINE);
        /*
         * Agrego el logo en el objeto apra guardar en la base de datos
         */
        establecimiento.setLogo(upload
                .addImage(optional.get().getRuc() + "/establecimientos/" + establecimiento.getCodigo(), "logo", logo));

        repository.save(establecimiento);
        return Respuesta.builder()
                .message("Establecimiento registrado con exito")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta update(Establecimiento establecimiento) {

        Optional<Establecimiento> optional = repository.findById(establecimiento.getIdEstablecimiento());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("El ruc indicado no existe")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getCodigo().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el codigo del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getWeb().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue la pagina web de su empresa")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getNombreComercial().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre comercial de si establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getDireccion().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue la direccion del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (establecimiento.getEmail().isEmpty()) {
            return Respuesta.builder()
                    .message("Agergue el email del establecimiento")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        establecimiento.setFkEmisor(optional.get().getFkEmisor());
        establecimiento.setStatus(Status.ONLINE);
        establecimiento.setLogo(optional.get().getLogo());
        repository.save(establecimiento);
        return Respuesta.builder()
                .message("Cambios guardados correctamente")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta changeStatus(Integer idEstablecimiento, Status status) {
        Optional<Establecimiento> optional = repository.findById(idEstablecimiento);

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

    @Override
    public List<EstablecimientoDto> getAllByRuc(String ruc) {
        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return null;
        }

        List<Establecimiento> lista = repository.findByFkEmisor(optional.get());
        if (lista.isEmpty()) {
            return null;
        }

        List<EstablecimientoDto> listaDto = lista.stream()
                .map(establecimiento -> modelMapper.map(establecimiento, EstablecimientoDto.class))
                .collect(Collectors.toList());

        return listaDto;
    }

    @Override
    public Page<EstablecimientoPageDto> getAll(Pageable pageable) {
        Page<Establecimiento> page = repository.findAll(pageable);
        if (!page.isEmpty()) {
            return page.map(emisor -> modelMapper.map(emisor, EstablecimientoPageDto.class));
        } else {
            return null;
        }
    }

    @Override
    public Respuesta getById(Integer idEstablecimiento) {

        Optional<Establecimiento> optional = repository.findById(idEstablecimiento);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("No existe ese registro")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .content(new Object[] { modelMapper.map(optional.get(), EstablecimientoPageDto.class) })
                .build();
    }

    @Override
    public Respuesta updateLogo(String ruc, Integer idEstablecimiento, MultipartFile logo) {

        Optional<Emisor> optionalEmisor = emisorRepository.findByRuc(ruc);

        if (!optionalEmisor.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el registro del emisor")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        Optional<Establecimiento> optional = repository.findById(idEstablecimiento);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("No existe ese registro")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (logo.isEmpty()) {
            return Respuesta.builder()
                    .message("Debe agregar el logotipo")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        optional.get().setLogo(
                upload.addImage(
                        optional.get().getFkEmisor().getRuc() + "/establecimientos/" + optional.get().getCodigo(),
                        "logo", logo));
        repository.save(optional.get());

        return Respuesta.builder()
                .message("Logotipo actualizado con exito")
                .type(RespuestaType.SUCCESS)
                .build();
    }

}
