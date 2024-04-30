package softech.apifacturacion.persistence.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.service.PtoEmisionService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/puntoEmision")
public class PtoEmisionController {

    private static final Logger logger = LoggerFactory.getLogger(PtoEmisionController.class);

    @Autowired
    private PtoEmisionService service;

    @PostMapping("/save/{ruc}")
    public ResponseEntity<Respuesta> save(@PathVariable("ruc") String ruc,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("codigo") String codigo,
            @RequestParam("secFactura") String secFactura,
            @RequestParam("secLiquidacion") String secLiquidacion,
            @RequestParam("secCredito") String secCredito,
            @RequestParam("secDebito") String secDebito,
            @RequestParam("secRemision") String secRemision,
            @RequestParam("secRetencion") String secRetencion) {

        try {
            Cajero cajero = Cajero.builder()
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .email(email)
                    .identificacion(identificacion)
                    .build();

            PtoEmision ptoEmision = PtoEmision.builder()
                    .codigo(codigo)
                    .secuenciaFactura(secFactura)
                    .secuenciaLiquidacion(secLiquidacion)
                    .secuencialCredito(secCredito)
                    .secuencialDebito(secDebito)
                    .secuencialRemision(secRemision)
                    .secuencialRetencion(secRetencion)
                    .fkCajero(cajero)
                    .build();

            Respuesta response = service.save(ruc, ptoEmision);
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
            logger.error("Error al generar el nuevo punto de emision: " + e);
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor al generar el nuevo punto de emision")
                    .build());
        }
    }

}
