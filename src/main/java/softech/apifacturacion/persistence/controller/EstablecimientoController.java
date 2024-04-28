package softech.apifacturacion.persistence.controller;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Establecimiento;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.service.EstablecimientoService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/establecimiento")
public class EstablecimientoController {

    private static final Logger logger = LoggerFactory.getLogger(EstablecimientoController.class);

    @Autowired
    private EstablecimientoService service;

    @PostMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("ruc") String ruc,
            @RequestParam("nombreEstablecimiento") String nombreEstablecimiento,
            @RequestParam("codigoEstablecimiento") String codigoEstablecimiento,
            @RequestParam("webEmpresa") String webEmpresa,
            @RequestParam("nombreComercial") String nombreComercial,
            @RequestParam("direccionEstablecimiento") String direccionEstablecimiento,
            @RequestParam("emailEstablecimiento") String emailEstablecimiento,
            @RequestParam("logoEstablecimiento") MultipartFile logoEstablecimiento) {

        try {
            Establecimiento establecimiento = Establecimiento.builder()
                    .nombre(nombreEstablecimiento)
                    .codigo(codigoEstablecimiento)
                    .web(webEmpresa)
                    .nombreComercial(nombreComercial)
                    .direccion(direccionEstablecimiento)
                    .email(emailEstablecimiento)
                    .build();

            Respuesta response = service.save(ruc, establecimiento, logoEstablecimiento);

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
            logger.error("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e.getCause())
                    .build());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Respuesta> update(@RequestParam("idEstablecimiento") String idEstablecimiento,
            @RequestParam("nombreEstablecimiento") String nombreEstablecimiento,
            @RequestParam("codigoEstablecimiento") String codigoEstablecimiento,
            @RequestParam("webEmpresa") String webEmpresa,
            @RequestParam("nombreComercial") String nombreComercial,
            @RequestParam("direccionEstablecimiento") String direccionEstablecimiento,
            @RequestParam("emailEstablecimiento") String emailEstablecimiento) {

        try {

            Establecimiento establecimiento = Establecimiento.builder()
                    .idEstablecimiento(Integer.parseInt(idEstablecimiento))
                    .nombre(nombreEstablecimiento)
                    .codigo(codigoEstablecimiento)
                    .web(webEmpresa)
                    .nombreComercial(nombreComercial)
                    .direccion(direccionEstablecimiento)
                    .email(emailEstablecimiento)
                    .build();
            Respuesta response = service.update(establecimiento);

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
            logger.error("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e.getCause())
                    .build());
        }
    }

    @PatchMapping("/updateLogo/{ruc}/{idEstablecimiento}")
    public ResponseEntity<Respuesta> updateLogo(@PathVariable("ruc") String ruc,
            @PathVariable("idEstablecimiento") String idEstablecimiento,
            @RequestParam("logo") MultipartFile logo) {
        try {

            Respuesta response = service.updateLogo(ruc, Integer.parseInt(idEstablecimiento), logo);

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
            logger.error("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e.getCause())
                    .build());
        }
    }

    @PatchMapping("/updateStatus")
    public ResponseEntity<Respuesta> changeStatus(@RequestParam("idEstablecimiento") String idEstablecimiento,
            @RequestParam("status") String status) {
        try {
            Respuesta response = service.changeStatus(Integer.parseInt(idEstablecimiento), Status.valueOf(status));
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
            logger.error("Error interno en el servidor en actualizar el status producto: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en actualizar el status producto: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/getData")
    public ResponseEntity<Page<EstablecimientoPageDto>> getAll(Pageable pageable) {
        try {
            Page<EstablecimientoPageDto> pagina = service.getAll(pageable);
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
    public ResponseEntity<List<EstablecimientoDto>> getAllByRuc(@PathVariable("ruc") String ruc) {
        try {
            List<EstablecimientoDto> pagina = service.getAllByRuc(ruc);
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
