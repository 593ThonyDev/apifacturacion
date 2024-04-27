package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

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
    private EmisorPageDto fkEmisor;

}
