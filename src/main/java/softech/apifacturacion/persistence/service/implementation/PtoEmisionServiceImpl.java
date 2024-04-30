package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.*;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.*;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class PtoEmisionServiceImpl implements PtoEmisionService {

        @Autowired
        private final PtoEmisionRepository repository;

        @Autowired
        private final EmisorRepossitory repEmisor;

        @Autowired
        private final CajeroRepository repCajero;

        @Autowired
        private CajeroService cajeroService;

        @Autowired
        private UserService userService;

        @Override
        public Respuesta save(String ruc, PtoEmision ptoEmision) {

                if (ruc.isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar el ruc del Emisor")
                                        .build();
                }
                if (ptoEmision.getFkCajero().getNombres().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar los nombres del cajero")
                                        .build();
                }
                if (ptoEmision.getFkCajero().getApellidos().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar los apellidos del cajero")
                                        .build();
                }
                if (ptoEmision.getFkCajero().getEmail().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar el email del cajero")
                                        .build();
                }
                if (ptoEmision.getCodigo().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar codigo del punto de emision de cajero")
                                        .build();
                }
                if (ptoEmision.getSecuenciaFactura().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("Debe agregar el secuencial de la ultima Factura emitida por este punto de emision")
                                        .build();
                }
                if (ptoEmision.getSecuenciaLiquidacion().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message(
                                                        "Debe agregar el secuencial de la ultima Liquidacion de Compra mitida por este punto de emision")
                                        .build();
                }
                if (ptoEmision.getSecuencialCredito().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message(
                                                        "Debe agregar el seccuencial de la Nota de Credito emitida por este punto de emision")
                                        .build();
                }
                if (ptoEmision.getSecuencialDebito().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message(
                                                        "Debe agregar el seccuencial de la ultima Nota de Debito emitida por este punto de emision")
                                        .build();
                }
                if (ptoEmision.getSecuencialRemision().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message(
                                                        "Debe agregar el seccuencial de la ultima Guia de Remision emitida por este punto de emision")
                                        .build();
                }
                if (ptoEmision.getSecuencialRetencion().isEmpty()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message(
                                                        "Debe agregar el seccuencial de la ultima Retension emitida por este punto de emision")
                                        .build();
                }

                Optional<Emisor> optionalEmisor = repEmisor.findByRuc(ruc);
                if (!optionalEmisor.isPresent()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("El emisor no se encuentra registrado")
                                        .build();
                }

                Optional<Cajero> optionalCajero = repCajero
                                .findByIdentificacion(ptoEmision.getFkCajero().getIdentificacion());
                if (optionalCajero.isPresent()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("El cajero ya se encuentra registrado")
                                        .build();
                } else {

                        Respuesta responseCajero = cajeroService.save(ptoEmision.getFkCajero());

                        if (responseCajero.getType() == RespuestaType.SUCCESS) {
                                UserRequestDto user = UserRequestDto.builder()
                                                .username(ptoEmision.getFkCajero().getIdentificacion())
                                                .fkEmisor(Emisor.builder().idEmisor(optionalEmisor.get().getIdEmisor())
                                                                .build())
                                                .email(ptoEmision.getFkCajero().getEmail())
                                                .apellidos(ptoEmision.getFkCajero().getApellidos())
                                                .nombres(ptoEmision.getFkCajero().getNombres())
                                                .role(Role.CAJERO)
                                                .password(UUID.randomUUID().toString())
                                                .build();
                                System.out.println("La contraseña es esta: " + user.getPassword().toString());

                                Respuesta responseUser = userService.registerPasswordShow(user);
                                if (responseUser.getType() == RespuestaType.SUCCESS) {
                                        ptoEmision.setNombre(ptoEmision.getFkCajero().getIdentificacion() + " - "
                                                        + ptoEmision.getFkCajero().getApellidos() + " "
                                                        + ptoEmision.getFkCajero().getNombres());
                                        ptoEmision.setFkEmisor(optionalEmisor.get());
                                        ptoEmision.setFkCajero(
                                                        repCajero.findByIdentificacion(
                                                                        ptoEmision.getFkCajero().getIdentificacion())
                                                                        .get());

                                        ptoEmision.setStatus(Status.ONLINE);
                                        repository.save(ptoEmision);

                                        return Respuesta.builder()
                                                        .type(RespuestaType.SUCCESS)
                                                        .message("Punto de emision creado con exito")
                                                        .build();
                                } else {
                                        return Respuesta.builder()
                                                        .type(RespuestaType.WARNING)
                                                        .message(responseUser.getMessage())
                                                        .build();
                                }
                        } else {

                                return Respuesta.builder()
                                                .type(RespuestaType.WARNING)
                                                .message(responseCajero.getMessage())
                                                .build();
                        }
                }

        }

        @Override
        public Respuesta changeStatus(String ruc, Integer idPuntoEmision, Status status) {
                Optional<Emisor> optionalEmisor = repEmisor.findByRuc(ruc);
                if (!optionalEmisor.isPresent()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("El emisor no se encuentra registrado")
                                        .build();
                }

                Optional<PtoEmision> optional = repository.findById(idPuntoEmision);
                if (!optional.isPresent()) {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("El cajero no se encuentra registrado")
                                        .build();
                }

                UserStatus userStatus;
                if (status == Status.OFFLINE) {
                        optional.get().setStatus(Status.OFFLINE);
                        optional.get().getFkCajero().setStatus(Status.OFFLINE);
                        userStatus = UserStatus.OFFLINE;

                } else {
                        optional.get().setStatus(Status.ONLINE);
                        optional.get().getFkCajero().setStatus(Status.ONLINE);
                        userStatus = UserStatus.ONLINE;
                }

                Respuesta response = userService.changeStatus(optional.get().getFkCajero().getIdentificacion(),
                                userStatus);

                if (response.getType() == RespuestaType.SUCCESS) {
                        repository.save(optional.get());
                        return Respuesta.builder()
                                        .type(RespuestaType.SUCCESS)
                                        .message("Status del punto de emision y cajero fue eactualizado correctamente")
                                        .build();
                } else {
                        return Respuesta.builder()
                                        .type(RespuestaType.WARNING)
                                        .message("No se puedo actualizar el estado")
                                        .build();
                }

        }

}
