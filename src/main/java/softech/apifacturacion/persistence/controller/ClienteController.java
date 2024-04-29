package softech.apifacturacion.persistence.controller;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.service.ClienteService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/cliente")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService service;

    @PostMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("ruc") String ruc,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipoidentificacion") String tipoIdentificacion,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("direccion") String direccion,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono) {

        try {
            Cliente cliente = Cliente.builder()
                    .nombre(nombre)
                    .tipoIdentificacion(tipoIdentificacion)
                    .identificacion(identificacion)
                    .direccion(direccion)
                    .email(email)
                    .telefono(telefono)
                    .build();

            Respuesta response = service.save(cliente, ruc);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en la cofiguracion del cliente: " + e.getMessage());
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message("Error interno en el servidor en la cofiguracion del cliente")
                    .build());
        }
    }

    @GetMapping("/data/{idCliente}")
    public ResponseEntity<Respuesta> getDataByid(@PathVariable("idCliente") String idCliente) {
        try {
            Respuesta response = service.getbyid(Integer.parseInt(idCliente));
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .content(response.getContent())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en listar el cliente: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en listar el ciente: " + e.getMessage())
                    .build());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Respuesta> update(
            @RequestParam("idCliente") String idCliente,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipoidentificacion") String tipoIdentificacion,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("direccion") String direccion,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono) {

        try {
            Cliente cliente = Cliente.builder()
                    .idCliente(Integer.parseInt(idCliente))
                    .nombre(nombre)
                    .tipoIdentificacion(tipoIdentificacion)
                    .identificacion(identificacion)
                    .direccion(direccion)
                    .email(email)
                    .telefono(telefono)
                    .build();

            Respuesta response = service.update(cliente);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor al actualizar cliente: " + e.getMessage());
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message("Error interno en el servidor al actualizar cliente")
                    .build());
        }
    }

    @GetMapping("/{ruc}/{idCliente}")
    public ResponseEntity<Respuesta> getDataByruc(@PathVariable("idCliente") String idCliente,
            @PathVariable("ruc") String ruc) {
        try {
            Respuesta response = service.getbyruc(Integer.parseInt(idCliente), ruc);
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .content(response.getContent())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en listar el cliente: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en listar el ciente: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/getData")
    public ResponseEntity<Page<ClienteDto>> getAll(Pageable pageable) {
        try {
            Page<ClienteDto> pagina = service.getAll(pageable);
            if (pagina != null && pagina.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (pagina != null) {
                return ResponseEntity.ok(pagina);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/getData/{ruc}")
    public ResponseEntity<Page<ClienteRucDto>> getAllByRuc(@PathVariable("ruc") String ruc, Pageable pageable) {
        try {
            Page<ClienteRucDto> pagina = service.getAllByRuc(ruc, pageable);
            if (pagina != null && pagina.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (pagina != null) {
                return ResponseEntity.ok(pagina);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/search/{identificacion}")
    public ResponseEntity<List<ClienteDto>> findByIdentifiacion(@PathVariable("identificacion") String identificacion) {
        try {
            List<ClienteDto> list = service.findByIdentificacion(identificacion);
            if (list != null && list.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (list != null) {
                return ResponseEntity.ok(list);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search/{ruc}/{identificacion}")
    public ResponseEntity<List<ClienteRucDto>> findByIdentifiacionAndRuc(
            @PathVariable("identificacion") String identificacion, @PathVariable("ruc") String ruc) {
        try {
            List<ClienteRucDto> list = service.findByRucAndIdentificacion(ruc, identificacion);
            if (list != null && list.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (list != null) {
                return ResponseEntity.ok(list);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().build();
        }
    }

}
