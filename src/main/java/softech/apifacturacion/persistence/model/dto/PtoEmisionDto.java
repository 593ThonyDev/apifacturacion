package softech.apifacturacion.persistence.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtoEmisionDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer idPtoemision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String codigo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Status status;
}
