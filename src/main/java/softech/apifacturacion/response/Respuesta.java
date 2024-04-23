package softech.apifacturacion.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Respuesta {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object[] content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    RespuestaType type;

}