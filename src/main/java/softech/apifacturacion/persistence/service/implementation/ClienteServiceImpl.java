package softech.apifacturacion.persistence.service.implementation;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.*;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.ClienteService;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private final ClienteRepository repository;

    @Autowired
    private final EmisorRepossitory emisorRepository;

    @Autowired
    private final ModelMapper mapper;

    @Override
    public Page<ClienteDto> getAll(Pageable pageable) {
        Page<Cliente> page = repository.findAll(pageable);
        if (page.isEmpty()) {
            return null;
        }
        return page.map(emisor -> mapper.map(emisor, ClienteDto.class));
    }

    @Override
    public Page<ClienteRucDto> getAllByRuc(String ruc, Pageable pageable) {
        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);

        if (!optional.isPresent()) {
            return null;
        }

        Page<Cliente> page = repository.findByFkEmisor(optional.get(), pageable);
        if (page.isEmpty()) {
            return null;
        }

        return page.map(emisor -> mapper.map(emisor, ClienteRucDto.class));
    }

    @Override
    public Respuesta save(Cliente cliente, String ruc) {

        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el ruc indicado")
                    .build();
        }
        if (cliente.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre del cliente")
                    .build();
        }
        if (cliente.getTipoIdentificacion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("seleccione el tipo de identificacion")
                    .build();
        }
        if (cliente.getIdentificacion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("seleccione la identificacion")
                    .build();
        }
        if (cliente.getDireccion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una direccion")
                    .build();
        }
        if (cliente.getEmail().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un email")
                    .build();
        }
        if (cliente.getTelefono().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un numero telefonico")
                    .build();
        }
        cliente.setFkEmisor(optional.get());

        cliente.setCreated(new Fecha().fechaCreacion());
        // Este comando me da la fecha de Guayaquil//

        switch (cliente.getTipoIdentificacion()) {
            case "RUC":
                cliente.setTipoIdentificacion("04");
                break;
            case "CEDULA":
                cliente.setTipoIdentificacion("05");
                break;
            case "PASAPORTE":
                cliente.setTipoIdentificacion("06");
                break;
            case "CONSUMIDOR_FINAL":
                cliente.setTipoIdentificacion("07");
                break;
            case "EXTRANJERO":
                cliente.setTipoIdentificacion("08");
                break;
            default:
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("Tipo e identificacion no valido")
                        .build();
        }

        repository.save(cliente);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Cliente cliente) {

        Optional<Cliente> optional = repository.findById(cliente.getIdCliente());
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el cliente indicado")
                    .build();
        }
        if (cliente.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre del cliente")
                    .build();
        }
        if (cliente.getTipoIdentificacion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("seleccione el tipo de identificacion")
                    .build();
        }
        if (cliente.getIdentificacion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("seleccione la identificacion")
                    .build();
        }
        if (cliente.getDireccion().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar una direccion")
                    .build();
        }
        if (cliente.getEmail().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un email")
                    .build();
        }
        if (cliente.getTelefono().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un numero telefonico")
                    .build();
        }

        cliente.setFkEmisor(optional.get().getFkEmisor());

        switch (cliente.getTipoIdentificacion()) {
            case "RUC":
                cliente.setTipoIdentificacion("04");
                break;
            case "CEDULA":
                cliente.setTipoIdentificacion("05");
                break;
            case "PASAPORTE":
                cliente.setTipoIdentificacion("06");
                break;
            case "CONSUMIDOR_FINAL":
                cliente.setTipoIdentificacion("07");
                break;
            case "EXTRANJERO":
                cliente.setTipoIdentificacion("08");
                break;
            default:
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("Tipo e identificacion no valido")
                        .build();
        }

        repository.save(cliente);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("cliente actualizado con exito")
                .build();

    }

    @Override
    public Respuesta getbyid(Integer idCliente) {
        Optional<Cliente> optionalClient = repository.findById(idCliente);
        if (!optionalClient.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el producto")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        return Respuesta.builder()
                .content(new Object[] { mapper.map(optionalClient.get(), ClienteDto.class) })
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta getbyruc(Integer idCliente, String ruc) {
        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el ruc")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        Optional<Cliente> optionalClient = repository.findById(idCliente);
        if (!optionalClient.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el cliente")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        return Respuesta.builder()
                .content(new Object[] { mapper.map(optionalClient.get(), ClienteRucDto.class) })
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public List<ClienteRucDto> findByRucAndIdentificacion(String ruc, String identificacion) {

        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return null;
        }

        List<Cliente> lista = repository.findByIdentificacion(identificacion);
        if (lista.isEmpty()) {
            return null;
        }

        List<ClienteRucDto> listaDto = lista.stream()
                .map(establecimiento -> mapper.map(establecimiento, ClienteRucDto.class))
                .collect(Collectors.toList());

        return listaDto;
    }

    @Override
    public List<ClienteDto> findByIdentificacion(String identificacion) {

        List<Cliente> lista = repository.findByIdentificacion(identificacion);
        if (lista.isEmpty()) {
            return null;
        }

        List<ClienteDto> listaDto = lista.stream()
                .map(establecimiento -> mapper.map(establecimiento, ClienteDto.class))
                .collect(Collectors.toList());

        return listaDto;
    }

}
