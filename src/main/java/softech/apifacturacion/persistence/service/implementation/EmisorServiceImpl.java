package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;
import java.util.concurrent.*;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.email.EmailSender;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.EmisorDto;
import softech.apifacturacion.persistence.model.dto.EmisorPageDto;
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
    Directory directory;

    @Autowired
    UserService userService;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private final ModelMapper mapper;

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
            emisor.setStatus(Status.CONFIGURATE);
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
                    .password(password)
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
    public Respuesta configurateEmisor(Emisor emisor, MultipartFile logo, MultipartFile firma) {
        Optional<Emisor> optional = repository.findByRuc(emisor.getRuc());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro con el ruc indicado")
                    .build();
        }

        // CONFIGURACION DE LA RAZON SOCIAL
        if (emisor.getRazonSocial().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el email para recibir los comprobantes generados")
                    .build();
        } else {
            optional.get().setRazonSocial(emisor.getRazonSocial());
        }

        // CONFIGURACION DEL NOMBRE COMERCIAL
        if (emisor.getNombreComercial().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el nombre comercial de la empresa")
                    .build();
        } else {
            optional.get().setNombreComercial(emisor.getNombreComercial());
            ;
        }

        // CONFIGURACION DE LA DIRECCION DE LA MATRIZ
        if (emisor.getDireccionMatriz().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el nombre comercial de la empresa")
                    .build();
        } else {
            optional.get().setDireccionMatriz(emisor.getDireccionMatriz());
        }

        // CONFIGURACION DEL EMISOR SI O NO ESTA OBLIGADO A LLEVAR CONTABILIDAD
        if (emisor.getObligadoContabilidad().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe seleccionar si esta obligado a llevar contabilidad")
                    .build();
        } else {
            optional.get().setObligadoContabilidad(emisor.getObligadoContabilidad());
        }

        // CONFIGURACION DEL AGENTE DE RETENCION
        if (emisor.getAgenteRetencion().isEmpty()) {
            optional.get().setAgenteRetencion(null);
        } else {
            optional.get().setAgenteRetencion(emisor.getAgenteRetencion());
        }

        // CONFIGURACION DEL REGIMEN DE LA MICROEMPRESA
        if (emisor.getRegimenMicroempresa().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe seleccionar el regimen para poder emitir los comprobantes electronicos")
                    .build();
        } else {
            optional.get().setRegimenMicroempresa(emisor.getRegimenMicroempresa());
        }

        // CONFIGURACION DEL TIPO DE CONTRIBUYENTE
        if (emisor.getTipoContribuyente().isEmpty()) {
            optional.get().setTipoContribuyente(null);
        } else {
            optional.get().setTipoContribuyente(emisor.getTipoContribuyente());
        }

        // CONFIGURACION DE LA CLAVE DE LA FIRMA ELECTRONICA
        if (emisor.getPassFirma().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la clave de la firma electronica")
                    .build();
        } else {
            optional.get().setPassFirma(emisor.getPassFirma());
        }

        // CONFIGURACION DEL DIRECTORIO PARA EL ALMACENAMIENTO DE LOS DOCUMENTOS
        optional.get().setDirAutorizados(directory.createDirectory(optional.get().getRuc()));

        // CONFIGURACION DEL LOGOTIPO DE LA EMPRESA
        if (logo.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el logotipo de la empresa")
                    .build();
        } else {
            optional.get()
                    .setLogo(imageUpload.addImage(optional.get().getRuc() + "/configuration", optional.get().getRuc(),
                            logo));
        }

        // CONFIGURACION DE LA FIRMA ELECTRONICA DE LA EMPRESA
        if (firma.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la firma electronica")
                    .build();
        } else {
            optional.get()
                    .setDirFirma(firmaUpload.addSignature(optional.get().getRuc() + "/configuration",
                            optional.get().getRuc(), firma));
        }

        // CONFIGURACION DEL STATUS A ONLINE
        optional.get().setStatus(Status.OFFLINE);

        /*
         * Se debe enviar el mail al usuario los datos de la empresa que ha sido
         * actualizado
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            emailSender.enviarCorreo(
                    optional.get().getCorreoRemitente(),
                    appName + " - LA CONFIGURACION DE SU NEGOCIO FUE REALIZADA CORRECTAMENTE",
                    "El mundo se digitaliza cada vez más y más, obligandonos a implementar nuevas tecnologías y herramientas en nuestro día a día. Una de ellas es la facturación electrónica en el mundo contable."
                            + "<br>"
                            + "La contabilidad de tu negocio nunca ha sido tan facil con nosotros, gracias por preferirnos."
                            + "<br><br>" +
                            "Si usted no ha modificado la cuenta de empresa contactase manera urgente con nuestro soporte técnico, respondiendo este email, asi solucionaremos este evento, para asi no tener inconvenientes de que afecten a su seguridad en el internet.",
                    optional.get().getRazonSocial());
        });

        executorService.shutdown();

        repository.save(optional.get());

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro actualizado correctamente")
                .build();
    }

    @Override
    public Respuesta changeStatus(String ruc, Status status) {
        Optional<Emisor> optional = repository.findByRuc(ruc);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro indicado")
                    .build();
        }

        optional.get().setStatus(status);

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Estado del emisor actualizado correctamente")
                .build();
    }

    @Override
    public Page<EmisorPageDto> getAll(Pageable pageable) {
        Page<Emisor> page = repository.findAll(pageable);
        if (!page.isEmpty()) {
            return page.map(emisor -> mapper.map(emisor, EmisorPageDto.class));
        } else {
            return null;
        }
    }

    @Override
    public Respuesta getDataByRuc(String ruc) {

        Optional<Emisor> optional = repository.findByRuc(ruc);
        
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro indicado")
                    .build();
        }
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .content(new Object[] { mapper.map(optional.get(), EmisorDto.class) })
                .build();
    }

}
