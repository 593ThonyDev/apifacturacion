package softech.apifacturacion.persistence.controller;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.enums.Role;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.service.UserService;
import softech.apifacturacion.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/facturacion/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @PostMapping("/registerEmisor")
    public ResponseEntity<Respuesta> register(
            @RequestParam("fkEmisor") String fkEmisor,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos) {

        try {
            Emisor emisor = Emisor.builder()
                    .idEmisor(Integer.parseInt(fkEmisor))
                    .build();

            SignUpEmisorDto companyDto = SignUpEmisorDto.builder()
                    .fkEmisor(emisor)
                    .username(username)
                    .password(password.toCharArray())
                    .email(email)
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .role(Role.EMISOR)
                    .build();

            Respuesta response = userService.register(companyDto);

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
                    .message("Error interno del servidor")
                    .build());
        }
    }

}