package softech.apifacturacion.error;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class ErrorDto {

    private String message;
}