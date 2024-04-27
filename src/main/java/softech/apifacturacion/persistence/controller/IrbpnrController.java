package softech.apifacturacion.persistence.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Irbpnr;
import softech.apifacturacion.persistence.service.IrbpnrService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/Irbpnr")
public class IrbpnrController {

    private static final Logger logger = LoggerFactory.getLogger(IrbpnrController.class);

    @Autowired
    private IrbpnrService service;

    @PostMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("nombre") String nombre,
            @RequestParam("codigoPorcentaje") String codigoPorcentaje,
            @RequestParam("tarifa") String tarifa) {

        try {

            Irbpnr irbpnr =Irbpnr.builder()
                    .nombre(nombre)
                    .codigoPorcentaje(codigoPorcentaje)
                    .tarifa(Double.parseDouble(tarifa))
                    .build();

            Respuesta response = service.save(irbpnr);

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
            @RequestParam("idIrbpnr") String idIrbpnr,
            @RequestParam("nombre") String nombre,
            @RequestParam("codigoPorcentaje") String codigoPorcentaje,
            @RequestParam("tarifa") String tarifa) {

        try {

            Irbpnr irbpnr =Irbpnr.builder()
                    .idIrbpnr(Integer.parseInt(idIrbpnr))
                    .nombre(nombre)
                    .codigoPorcentaje(codigoPorcentaje)
                    .tarifa(Double.parseDouble(tarifa))
                    .build();

            Respuesta response = service.update(irbpnr);

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
            @RequestParam("idIrbpnr") String idIrbpnr,
            @RequestParam("status") String statusRequest) {

        try {

            Respuesta response = service.changeStatus(Integer.parseInt(idIrbpnr),
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
    public ResponseEntity<Page<Irbpnr>> getAll(Pageable pageable) {
        try {
            Page<Irbpnr> pagina = service.getAll(pageable);
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
