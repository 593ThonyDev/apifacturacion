package softech.apifacturacion.persistence.service.implementation;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.exception.AppException;
import softech.apifacturacion.persistence.enums.UserStatus;
import softech.apifacturacion.persistence.model.User;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.repository.UserRepository;
import softech.apifacturacion.persistence.service.UserService;
import softech.apifacturacion.response.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto login(CredentialsDto credentialsDto) {
        return null;
    }

    @Override
    public Respuesta register(SignUpEmisorDto userDto) {

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Este usuario no esta disponible", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFkEmisor(userDto.getFkEmisor());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setEmail(userDto.getEmail());
        user.setNombres(userDto.getNombres());
        user.setApellidos(userDto.getApellidos());
        user.setRole(userDto.getRole());
        user.setStatus(UserStatus.UPDATE_PASS);

        userRepository.save(user);

        return Respuesta.builder()
                .message("Datos registrados correctamente")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public UserDto findByLogin(String login) {
        // User user = userRepository.findByLogin(login)
        // .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        // return userMapper.toUserDto(user);
        return null;
    }

}