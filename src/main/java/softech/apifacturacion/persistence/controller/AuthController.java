package softech.apifacturacion.persistence.controller;

import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturacion/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private EmisorService emisorService;

    @PostMapping("/registerEmisor")
    public ResponseEntity<Respuesta> register(
            @RequestParam("ruc") String ruc,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("plan") String fkplan) {

        try {

            Emisor emisor = Emisor.builder()
                    .ruc(ruc)
                    .correoRemitente(email)
                    .fkPlan(Plan.builder().idPlan(Integer.parseInt(fkplan)).build())
                    .build();

            Respuesta response = emisorService.registerEmisor(emisor, nombres, apellidos, password);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e)
                    .build());
        }
    }

}