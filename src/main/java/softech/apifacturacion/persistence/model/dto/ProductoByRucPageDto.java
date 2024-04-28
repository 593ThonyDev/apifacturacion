package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoByRucPageDto {
    
    private Integer idProducto;
    private String nombre;
    private String codPrincipal;
    private String codAuxiliar;
    private String img1;
    private Status status;

}
