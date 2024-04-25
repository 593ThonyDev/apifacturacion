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
    private char[] password;
    private String email;
    private String nombres;
    private String apellidos;
    private String login;
    private Role role;

}