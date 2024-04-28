package softech.apifacturacion.persistence.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.model.Cliente;
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
                    .Identificacion(identificacion)
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

    @GetMapping("/getData/{idCliente}")
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
                    .Identificacion(identificacion)
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

    @GetMapping("/getData/{ruc}/{idCliente}")
    public ResponseEntity<Respuesta> getDataByruc(@PathVariable("idCliente") String idCliente,@PathVariable("ruc") String ruc) {
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



}
