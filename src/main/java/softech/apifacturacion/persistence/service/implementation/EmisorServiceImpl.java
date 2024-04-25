package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.UserRequestDto;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.*;
import softech.apifacturacion.response.*;
import softech.apifacturacion.upload.*;

@Service
@RequiredArgsConstructor
public class EmisorServiceImpl implements EmisorService {

    private static final Logger logger = LoggerFactory.getLogger(EmisorServiceImpl.class);

    @Autowired
    EmisorRepossitory repository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    UploadImage imageUpload;

    @Autowired
    UploadSignature firmaUpload;

    @Autowired
    UserService userService;

    @Override
    public Respuesta registerEmisor(Emisor emisor, String nombres, String apellidos, String password) {
    
        Optional<Emisor> optional = repository.findByRuc(emisor.getRuc());
    
        if (optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("El ruc ya se encuentra registrado en nuestro sistema")
                    .build();
        }
    
        if (emisor.getRuc().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el ruc correspondiente")
                    .build();
        }
    
        if (emisor.getCorreoRemitente().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el email para recibir los comprobantes generados")
                    .build();
        }
    
        try {
            // Verificar si el plan existe en la base de datos
            Optional<Plan> optionalPlan = planRepository.findById(emisor.getFkPlan().getIdPlan());
            if (!optionalPlan.isPresent()) {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("El plan especificado no existe")
                        .build();
            }
    
            Plan plan = optionalPlan.get();
    
            // Configurar emisor
            emisor.setAmbiente(AmbienteSri.TEST.getUrl());
            emisor.setTipoEmision(TipoEmision.NORMAL.getCodigo());
            emisor.setStatus(Status.ONLINE);
            emisor.setFkPlan(plan);
            emisor.setCantidadContratada(plan.getCantidad());
            emisor.setCantidadUsada(0);
            emisor.setFechaInicio(new Fecha().fechaCreacion());
            emisor.setFechaFin(new Fecha().addMeses(emisor.getFechaInicio(), Integer.parseInt(plan.getPeriodo())));
            emisor.setCreated(new Fecha().fechaCreacion());

            repository.save(emisor);
    
            // Construir DTO de usuario y registrar
            UserRequestDto user = UserRequestDto.builder()
                    .fkEmisor(emisor)
                    .username(emisor.getRuc())
                    .password(password.toCharArray())
                    .email(emisor.getCorreoRemitente())
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .role(Role.EMISOR)
                    .build();
    
            // Registrar usuario
            Respuesta response = userService.register(user);
    
            // Manejar errores al registrar usuario
            if (response.getType() == RespuestaType.WARNING) {
                // Eliminar emisor de la base de datos si hay un error al registrar el usuario
                repository.deleteById(emisor.getIdEmisor());
                return response;
            }
    
            return Respuesta.builder()
                    .type(RespuestaType.SUCCESS)
                    .message("Registro guardado correctamente")
                    .build();
        } catch (Exception e) {
            // Manejar cualquier otro error interno
            logger.error("Error interno del servidor: " + e.getMessage());
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Error interno del servidor: " + e.getMessage())
                    .build();
        }
    }
    
    @Override
    public Respuesta updateEmisor(Emisor emisor, MultipartFile logo, MultipartFile firma) {
        Optional<Emisor> optional = repository.findByRuc(emisor.getRuc());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro con el ruc indicado")
                    .build();
        }
        if (emisor.getRazonSocial().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el email para recibir los comprobantes generados")
                    .build();
        } else {
            optional.get().setRazonSocial(emisor.getRazonSocial());
        }
        if (emisor.getNombreComercial().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el nombre comercial de la empresa")
                    .build();
        } else {
            optional.get().setNombreComercial(emisor.getNombreComercial());
            ;
        }
        if (emisor.getDireccionMatriz().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el nombre comercial de la empresa")
                    .build();
        } else {
            optional.get().setDireccionMatriz(emisor.getDireccionMatriz());
        }
        if (emisor.getObligadoContabilidad().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe seleccionar si esta obligado a llevar contabilidad")
                    .build();
        } else {
            optional.get().setObligadoContabilidad(emisor.getObligadoContabilidad());
        }
        if (emisor.getTipoContribuyente().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe seleccionar el tipo de contribuyente")
                    .build();
        } else {
            optional.get().setTipoContribuyente(emisor.getTipoContribuyente());
        }
        if (emisor.getPassFirma().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la clave de la firma electronica")
                    .build();
        } else {
            optional.get().setPassFirma(emisor.getPassFirma());
        }
        if (logo.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el logotipo de la empresa")
                    .build();
        } else {
            optional.get().setDirLogo(imageUpload.addImage(optional.get().getRuc(), "logoEmisor", logo));
        }
        if (firma.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la firma electronica")
                    .build();
        } else {
            optional.get()
                    .setDirFirma(firmaUpload.addSignature(optional.get().getRuc(), optional.get().getRuc(), firma));
        }

        /*
         * Se debe enviar el mail al usuario los datos de la empresa que ha sido
         * actualizado
         */

        repository.save(optional.get());

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro actualizado correctamente")
                .build();
    }

}
