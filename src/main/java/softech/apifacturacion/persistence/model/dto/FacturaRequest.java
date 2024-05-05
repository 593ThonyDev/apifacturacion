package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.model.DetalleFactura;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequest {

    private String ruc;
    private String cajeroIdentificacion;
    private String clienteIdentificacion;
    private String formaPago;
    private String propina;
    private DetalleFactura[] detalleFacturas;
}
