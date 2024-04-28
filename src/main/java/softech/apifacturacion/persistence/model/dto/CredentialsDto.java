package softech.apifacturacion.persistence.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDto {

    private String username;
    private char[] password;

}