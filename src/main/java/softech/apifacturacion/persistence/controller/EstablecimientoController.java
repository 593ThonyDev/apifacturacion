package softech.apifacturacion.persistence.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.service.EstablecimientoService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@RestController
@RequestMapping("/facturacion/v1/establecimiento")
public class EstablecimientoController {

    private static final Logger logger = LoggerFactory.getLogger(EstablecimientoController.class);

    @Autowired
    private EstablecimientoService service;

    @GetMapping("/{idEstablecimiento}")
    public ResponseEntity<Respuesta> getById(@PathVariable("idEstablecimiento") String idEstablecimiento) {
        try {
            Respuesta response = service.getById(Integer.parseInt(idEstablecimiento));
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .content(response.getContent())
                        .build());
            } else {
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e)
                    .build());
        }
    }

}
