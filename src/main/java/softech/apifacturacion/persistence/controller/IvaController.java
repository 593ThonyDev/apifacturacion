package softech.apifacturacion.persistence.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softech.apifacturacion.persistence.model.Iva;
import softech.apifacturacion.persistence.service.IvaService;
import softech.apifacturacion.response.*;
import softech.apifacturacion.status.Status;

@RestController
@RequestMapping("/facturacion/v1")
public class IvaController {

    private static final Logger logger = LoggerFactory.getLogger(IvaController.class);

    @Autowired
    private IvaService service;

    @PostMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("nombre") String nombre,
            @RequestParam("codigoPorcentaje") String codigoPorcentaje,
            @RequestParam("tarifa") String tarifa) {

        try {

            Iva iva = Iva.builder()
                    .nombre(nombre)
                    .codigoPorcentaje(codigoPorcentaje)
                    .tarifa(Double.parseDouble(tarifa))
                    .build();

            Respuesta response = service.save(iva);

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
            @RequestParam("idIva") String idIva,
            @RequestParam("nombre") String nombre,
            @RequestParam("codigoPorcentaje") String codigoPorcentaje,
            @RequestParam("tarifa") String tarifa) {

        try {

            Iva iva = Iva.builder()
                    .idIva(Integer.parseInt(idIva))
                    .nombre(nombre)
                    .codigoPorcentaje(codigoPorcentaje)
                    .tarifa(Double.parseDouble(tarifa))
                    .build();

            Respuesta response = service.update(iva);

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

    @PatchMapping("/changeStatus")
    public ResponseEntity<Respuesta> updateStatus(
            @RequestParam("idIva") String idIva,
            @RequestParam("status") String statusRequest) {

        try {

            Respuesta response = service.changeStatus(Integer.parseInt(idIva),
                    statusRequest == "ONLINE" ? Status.ONLINE : Status.OFFLINE);

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
    public ResponseEntity<Page<Iva>> getAll(Pageable pageable) {
        try {
            Page<Iva> pagina = service.getAll(pageable);
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
