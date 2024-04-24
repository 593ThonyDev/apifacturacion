package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.model.dto.CredentialsDto;
import softech.apifacturacion.persistence.model.dto.SignUpEmisorDto;
import softech.apifacturacion.persistence.model.dto.UserDto;
import softech.apifacturacion.response.Respuesta;

public interface UserService {
    public UserDto login(CredentialsDto credentialsDto);

    Respuesta register(SignUpEmisorDto SignUpEmisorDto);

    public UserDto findByLogin(String login);
}
