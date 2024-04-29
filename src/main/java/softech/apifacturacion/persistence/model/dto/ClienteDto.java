package softech.apifacturacion.persistence.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {

    private Integer idCliente;
    private String nombre;
    private String tipoIdentificacion;
    private String identificacion;
    private String direccion;
    private String email;
    private String telefono;
    private EmisorPageDto fkEmisor;

}
