package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmisorPageDto {
    Integer idEmisor;
    String ruc;
    String razonSocial;
    String nombreComercial;
    String logo;
    Status status;
}
