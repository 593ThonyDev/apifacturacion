package softech.apifacturacion.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import softech.apifacturacion.persistence.model.Factura;
import softech.apifacturacion.persistence.model.dto.FacturaRequest;
import softech.apifacturacion.persistence.service.FacturaService;
import softech.apifacturacion.response.Respuesta;
import softech.apifacturacion.response.RespuestaType;

@RestController
@RequestMapping("/facturacion/v1/factura")
public class FacturaController {

        @Autowired
        private FacturaService service;

        @PostMapping("/save")
        public ResponseEntity<Respuesta> save(
                        @RequestBody FacturaRequest facturaDto

        ) {     
                Factura factura = Factura.builder()
                .propina(Double.parseDouble(facturaDto.getPropina()))
                .formaPago(facturaDto.getFormaPago())
                .build();

                Respuesta response = service.save(facturaDto.getRuc(), facturaDto.getCajeroIdentificacion(), facturaDto.getClienteIdentificacion(), factura, facturaDto.getDetalleFacturas());
                if (response.getType() == RespuestaType.SUCCESS) {
                        return ResponseEntity.ok().body(Respuesta.builder()
                                        .message(response.getMessage())
                                        .build());
                } else {
                        return ResponseEntity.badRequest().body(Respuesta.builder()
                                        .message(response.getMessage())
                                        .build());

                }
        }

}
