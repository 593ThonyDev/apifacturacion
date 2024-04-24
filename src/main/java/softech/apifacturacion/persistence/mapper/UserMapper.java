package softech.apifacturacion.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import softech.apifacturacion.persistence.model.User;
import softech.apifacturacion.persistence.model.dto.SignUpEmisorDto;
import softech.apifacturacion.persistence.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    User signUpToUser(SignUpEmisorDto signUpDto);

}