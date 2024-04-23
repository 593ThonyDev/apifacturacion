package softech.apifacturacion.persistence.model.dto;

import softech.apifacturacion.persistence.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Integer idUser;
    private String nombres;
    private String apellidos;
    private String login;
    private String token;
    private Role role;

}