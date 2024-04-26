package softech.apifacturacion.persistence.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/emisor")
public class EmisorController {

    private static final Logger logger = LoggerFactory.getLogger(EmisorController.class);

    @Autowired
    private EmisorService service;

    @PatchMapping("/configuration")
    public ResponseEntity<Respuesta> configurateEmisor(
            @RequestParam("ruc") String ruc,
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("nombreCommercial") String nombreCommercial,
            @RequestParam("direccionMatriz") String direccionMatriz,
            @RequestParam("obligadoLlevarContabilidad") String obligadoLlevarContabilidad,
            @RequestParam("agenteResolucion") String agenteResolucion,
            @RequestParam("regimenMicroempresa") String regimenMicroempresa,
            @RequestParam("tipoContribuyente") String tipoContribuyente,
            @RequestParam("passFirma") String passFirma,
            @RequestParam("firma") MultipartFile firma,
            @RequestParam("logotipo") MultipartFile logotipo) {

        try {
            Emisor emisor = Emisor.builder()
                    .ruc(ruc)
                    .razonSocial(razonSocial)
                    .nombreComercial(nombreCommercial)
                    .direccionMatriz(direccionMatriz)
                    .obligadoContabilidad(obligadoLlevarContabilidad == "SI" ? "SI" : "NO")
                    .agenteRetencion(agenteResolucion)
                    .regimenMicroempresa(regimenMicroempresa)
                    .tipoContribuyente(tipoContribuyente)
                    .passFirma(passFirma)
                    .build();

            Respuesta response = service.configurateEmisor(emisor, logotipo, firma);

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
            logger.error("Error interno en el servidor en la cofiguracion del emisor: " + e.getMessage());
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message("Error interno en el servidor en la cofiguracion del emisor")
                    .build());
        }
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<Respuesta> changeStatus(@RequestParam("ruc") String ruc,
            @RequestParam("status") String status) {
        try {
            Respuesta response = service.changeStatus(ruc, Status.valueOf(status));
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en actualizar el status emisor: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en actualizar el status emisor: " + e.getMessage())
                    .build());
        }
    }

}
