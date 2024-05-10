package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.enums.UserStatus;
import softech.apifacturacion.persistence.model.dto.CredentialsDto;
import softech.apifacturacion.persistence.model.dto.UserRequestDto;
import softech.apifacturacion.persistence.model.dto.UserDto;
import softech.apifacturacion.response.Respuesta;

public interface UserService {

    Respuesta login(CredentialsDto credentialsDto);

    Respuesta register(UserRequestDto UserRequestDto);

    Respuesta registerAdmin(UserRequestDto UserRequestDto);

    Respuesta changeStatus(String username, UserStatus status);

    Respuesta registerPasswordShow(UserRequestDto UserRequestDto);

    public UserDto findByUsername(String login);

}
