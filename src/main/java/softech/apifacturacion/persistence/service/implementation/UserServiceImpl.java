package softech.apifacturacion.persistence.service.implementation;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.email.EmailSender;
import softech.apifacturacion.exception.AppException;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.mapper.UserMapper;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.UserService;
import softech.apifacturacion.response.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.*;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private EmailSender emailSender;

    private final UserMapper userMapper;

    @Autowired
    private final PtoEmisionRepository ptoEmisionRepository;

    @Autowired
    private final CajeroRepository cajeroRepository;

    @Autowired
    private final ModelMapper mapper;

    @Override
    public Respuesta login(CredentialsDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Usuario no registrado", HttpStatus.NOT_FOUND));

        if (user.getStatus() == UserStatus.OFFLINE) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Usuario bloqueado del sistema")
                    .build();
        }

        if (user.getFkEmisor() != null && user.getFkEmisor().getStatus() == Status.OFFLINE) {
            throw new AppException(
                    "La empresa " + user.getFkEmisor().getNombreComercial() + " está bloqueada en nuestro sistema",
                    HttpStatus.BAD_REQUEST);
        }

        List<Map<String, Object>> responseList = new ArrayList<>();

        if (user.getRole() == Role.EMISOR) {
            Emisor emisor = user.getFkEmisor();
            Map<String, Object> emisorMap = new HashMap<>();
            emisorMap.put("emisor", mapper.map(emisor, EmisorPageDto.class));
            responseList.add(emisorMap);
        } else if (user.getRole() == Role.CAJERO) {
            Emisor emisor = user.getFkEmisor();

            Optional<PtoEmision> ptoEmision = ptoEmisionRepository
                    .findByFkCajero(cajeroRepository.findByIdentificacion(user.getUsername()).get());

            if (ptoEmision.isPresent()) {
                Map<String, Object> emisorMap = new HashMap<>();
                emisorMap.put("emisor", mapper.map(emisor, EmisorPageDto.class));
                responseList.add(emisorMap);

                Map<String, Object> ptoEmisionMap = new HashMap<>();
                ptoEmisionMap.put("ptoEmision", mapper.map(ptoEmision.get(), PtoEmisionDto.class));
                responseList.add(ptoEmisionMap);
            }
        }
        if (user.getRole() == Role.ADMINISTRADOR
                && passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            UserDto userDto = userMapper.toUserDto(user);
            return Respuesta.builder()
                    .type(RespuestaType.SUCCESS)
                    .user(userDto)
                    .content(responseList.toArray())
                    .build();
        }

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            UserDto userDto = userMapper.toUserDto(user);
            return Respuesta.builder()
                    .type(RespuestaType.SUCCESS)
                    .user(userDto)
                    .content(responseList.toArray())
                    .build();
        } else {
            throw new AppException("Contraseña incorrecta", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Respuesta register(UserRequestDto userDto) {

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());

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

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            emailSender.enviarCorreo(
                    user.getEmail(),
                    appName + " - BIENVENIDO A NUESTRO SISTEMA DE FACTURACION ELECTRONICA",
                    "El mundo se digitaliza cada vez más, obligandonos a implementar nuevas tecnologías y herramientas en nuestro día a día. Una de ellas es la facturación electrónica en el mundo contable."
                            + "<br>"
                            + "Bienvenido a nuestra plataforma, el mundo contable nunca ha sido tan facil con nosotros"
                            + "<br><br>" +
                            "Si usted no ha creado esta cuenta contactase manera urgente, respondiendo este email, asi solucionaremos este evento, para asi no tener inconvenientes de que afecten a su seguridad en el internet.",
                    user.getNombres() + " " + user.getApellidos());
        });

        executorService.shutdown();

        return Respuesta.builder()
                .message("Datos registrados correctamente")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta registerAdmin(UserRequestDto userDto) {

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());

        if (optionalUser.isPresent()) {
            throw new AppException("Este usuario no esta disponible", HttpStatus.BAD_REQUEST);
        }

        userDto.setPassword(UUID.randomUUID().toString());

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setEmail(userDto.getEmail());
        user.setNombres(userDto.getNombres());
        user.setApellidos(userDto.getApellidos());
        user.setRole(Role.ADMINISTRADOR);
        user.setStatus(UserStatus.UPDATE_PASS);

        userRepository.save(user);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            emailSender.enviarCorreo(
                    user.getEmail(),
                    appName + " - BIENVENIDO A NUESTRO SISTEMA DE FACTURACION ELECTRONICA",
                    "El mundo se digitaliza cada vez más y más, obligandonos a implementar nuevas tecnologías y herramientas en nuestro día a día. Una de ellas es la facturación electrónica en el mundo contable."
                            + "<br>"
                            + "Bienvenido a nuestra plataforma, el mundo contrable nunca ha sido tan facil con nosotros"
                            + "<br><br>" + "Esta es su contraseña para acceder a nuestro sistema"
                            + "<br><br>"
                            + "<b style=\"font-size: 20px;\">" + userDto.getPassword().toString() + "</b>"
                            + "<br><br>" +
                            "Por favor acttualice su contraseña y no la comparta con nadie, para asi no tener inconvenientes de que afecten a su seguridad en el internet.",
                    user.getNombres() + " " + user.getApellidos());
        });

        executorService.shutdown();

        return Respuesta.builder()
                .message("Datos registrados correctamente")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta registerPasswordShow(UserRequestDto userDto) {

        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());

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

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            emailSender.enviarCorreo(
                    user.getEmail(),
                    appName + " - BIENVENIDO A NUESTRO SISTEMA DE FACTURACION ELECTRONICA",
                    "El mundo se digitaliza cada vez más y más, obligandonos a implementar nuevas tecnologías y herramientas en nuestro día a día. Una de ellas es la facturación electrónica en el mundo contable."
                            + "<br>"
                            + "Bienvenido a nuestra plataforma, el mundo contrable nunca ha sido tan facil con nosotros"
                            + "<br><br>" + "Esta es su contraseña para acceder a nuestro sistema"
                            + "<br><br>"
                            + "<b style=\"font-size: 20px;\">" + userDto.getPassword().toString() + "</b>"
                            + "<br><br>" +
                            "Por favor acttualice su contraseña y no la comparta con nadie, para asi no tener inconvenientes de que afecten a su seguridad en el internet.",
                    user.getNombres() + " " + user.getApellidos());
        });

        executorService.shutdown();

        return Respuesta.builder()
                .message("Datos registrados correctamente")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    @Override
    public Respuesta changeStatus(String username, UserStatus status) {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Usuario no registrado")
                    .build();
        }

        if (status == UserStatus.OFFLINE) {
            user.get().setStatus(UserStatus.ONLINE);
        } else {
            user.get().setStatus(UserStatus.ONLINE);
        }

        userRepository.save(user.get());

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Usuario actualizado con exito")
                .build();

    }

}