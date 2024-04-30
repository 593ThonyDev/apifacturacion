package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Role;
import softech.apifacturacion.persistence.model.Emisor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    private Emisor fkEmisor;
    private String username;
    private String password;
    private String email;
    private String nombres;
    private String apellidos;
    private Role role;

}