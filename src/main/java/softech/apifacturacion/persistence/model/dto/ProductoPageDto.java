package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Ice;
import softech.apifacturacion.persistence.model.Irbpnr;
import softech.apifacturacion.persistence.model.Iva;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPageDto {
    
    private Integer idProducto;
    private String nombre;
    private Status status;
    private String codPrincipal;
    private String codAuxiliar;
    private String img1;
    private Iva fkIva;
    private Ice fkIce;
    private Irbpnr fkIrbpnr;
    private EmisorPageDto fkEmisor;

}
