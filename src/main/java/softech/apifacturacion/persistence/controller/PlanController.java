package softech.apifacturacion.persistence.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.service.PlanService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/plan")
public class PlanController {

    private static final Logger logger = LoggerFactory.getLogger(IvaController.class);

    @Autowired
    private PlanService service;

    @PostMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("nombre") String nombre,
            @RequestParam("cantidad") String cantidad,
            @RequestParam("precio") String precio,
            @RequestParam("periodo") String periodo,
            @RequestParam("descripcion") String descripcion) {

        try {

            Plan plan = Plan.builder()
                    .nombre(nombre)
                    .cantidad(Integer.parseInt(cantidad))
                    .precio(Double.parseDouble(precio))
                    .periodo(periodo)
                    .descripcion(descripcion)
                    .status(Status.ONLINE)
                    .build();

            Respuesta response = service.save(plan);

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
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor")
                    .build());
        }

    }

    @PatchMapping("/update")
    public ResponseEntity<Respuesta> update(
            @RequestParam("idPlan") String idPlan,
            @RequestParam("nombre") String nombre,
            @RequestParam("cantidad") String cantidad,
            @RequestParam("precio") String precio,
            @RequestParam("periodo") String periodo,
            @RequestParam("status") String status,
            @RequestParam("descripcion") String descripcion) {

        try {

            Plan plan = Plan.builder()
                    .idPlan(Integer.parseInt(idPlan))
                    .nombre(nombre)
                    .cantidad(Integer.parseInt(cantidad))
                    .precio(Double.parseDouble(precio))
                    .periodo(periodo)
                    .descripcion(descripcion)
                    .status(Status.valueOf(status.toUpperCase()))
                    .build();

            Respuesta response = service.update(plan);

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
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor")
                    .build());
        }

    }

    @GetMapping("/getData")
    public ResponseEntity<Page<Plan>> getAll(Pageable pageable) {
        try {
            Page<Plan> pagina = service.getAll(pageable);
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

}
