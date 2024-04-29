package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.model.dto.CredentialsDto;
import softech.apifacturacion.persistence.model.dto.UserRequestDto;
import softech.apifacturacion.persistence.model.dto.UserDto;
import softech.apifacturacion.response.Respuesta;

public interface UserService {
    public UserDto login(CredentialsDto credentialsDto);

    Respuesta register(UserRequestDto UserRequestDto);

    public UserDto findByUsername(String login);
}
