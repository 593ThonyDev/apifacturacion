package softech.apifacturacion.persistence.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.dto.EmisorPageDto;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/emisor")
public class EmisorController {

    private static final Logger logger = LoggerFactory.getLogger(EmisorController.class);

    @Autowired
    private EmisorService service;

    @PatchMapping("/configuration")
    @PreAuthorize("hasAnyAuthority('EMISOR')")
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
    @PreAuthorize("hasAnyAuthority('EMISOR','ADMINISTRADOR')")
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

    @GetMapping("/getData")
    public ResponseEntity<Page<EmisorPageDto>> getAll(Pageable pageable) {
        try {
            Page<EmisorPageDto> pagina = service.getAll(pageable);
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
    public ResponseEntity<Respuesta> getDataByRuc(@PathVariable("ruc") String ruc) {
        try {
            Respuesta response = service.getDataByRuc(ruc);
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
            logger.error("Error interno en el servidor en listar al emisor: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en listar al emisor: " + e.getMessage())
                    .build());
        }
    }
}
