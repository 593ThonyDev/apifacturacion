package softech.apifacturacion.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.model.PtoEmision;
import softech.apifacturacion.persistence.service.FacturaService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@RestController
@RequestMapping("/facturacion/v1/factura")
public class FacturaController {

    @Autowired
    private FacturaService service;

    @GetMapping("/claveAcceso/{ruc}/{ptoEmision}")
    public ResponseEntity<Respuesta> generateClaveAcceso(@PathVariable("ruc") String ruc,
            @PathVariable("ptoEmision") String ptoEmision) {
        
        Respuesta response = service.generateClaveAcceso(ruc, PtoEmision.builder().idPtoemision(Integer.parseInt(ptoEmision)).build());
        if (response.getType() == RespuestaType.SUCCESS) {
            return ResponseEntity.ok().body(Respuesta.builder()
                    .content(response.getContent())
                    .build());
        } else {
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message(response.getMessage())
                    .build());
        }
    }

}
