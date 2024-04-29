package softech.apifacturacion.persistence.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRucDto {
    private Integer idCliente;
    private String nombre;
    private String tipoIdentificacion;
    private String identificacion;
    private String direccion;
    private String email;
    private String telefono;
}
