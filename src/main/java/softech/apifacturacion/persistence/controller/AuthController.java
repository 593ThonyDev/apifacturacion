package softech.apifacturacion.persistence.controller;

import softech.apifacturacion.config.UserAuthenticationProvider;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.model.dto.CredentialsDto;
import softech.apifacturacion.persistence.model.dto.UserDto;
import softech.apifacturacion.persistence.model.dto.UserRequestDto;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.persistence.service.UserService;
import softech.apifacturacion.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/facturacion/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private EmisorService emisorService;

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/registerEmisor")
    public ResponseEntity<Respuesta> register(
            @RequestParam("ruc") String ruc,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("plan") String fkplan) {

        try {

            Emisor emisor = Emisor.builder()
                    .ruc(ruc)
                    .correoRemitente(email)
                    .fkPlan(Plan.builder().idPlan(Integer.parseInt(fkplan)).build())
                    .build();

            Respuesta response = emisorService.registerEmisor(emisor, nombres, apellidos, password);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e)
                    .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Respuesta> registerAdmin(
            @RequestParam("cedula") String cedula,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email) {

        try {

            UserRequestDto user = UserRequestDto.builder()
                    .username(cedula)
                    .email(email)
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .build();

            Respuesta response = userService.registerAdmin(user);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno del servidor: " + e)
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Respuesta> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        try {
            CredentialsDto credentials = CredentialsDto.builder()
                    .username(username)
                    .password(password)
                    .build();

            Respuesta response = userService.login(credentials);

            if (response.getType() == RespuestaType.SUCCESS) {
                UserDto userDto = (UserDto) response.getUser();

                String token = userAuthenticationProvider.createToken(userDto);
                userDto.setToken(token);
                return ResponseEntity.ok(Respuesta.builder()
                        .user(response.getUser())
                        .content(response.getContent())
                        .build());
            } else {
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message(e.getMessage().toString())
                    .build());
        }
    }

}