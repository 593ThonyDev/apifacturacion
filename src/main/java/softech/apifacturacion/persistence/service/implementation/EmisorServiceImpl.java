package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.SignUpEmisorDto;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.*;
import softech.apifacturacion.response.*;
import softech.apifacturacion.upload.*;

public class EmisorServiceImpl implements EmisorService {

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
                    .message("Debe agregar el ruc correspodiente")
                    .build();
        }

        if (emisor.getCorreoRemitente().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el email para recibir los comprobantes generados")
                    .build();
        }

        /*
         * Configuracion ambiente para la emision de los comprobantes
         * 
         * @param ambiente 1 es para PRUEBAS
         * 
         * @param ambiente 2 es para PRODUCCION
         */
        emisor.setAmbiente("1");

        /*
         * Configuracion de emision de los comprobantes
         */
        emisor.setTipoEmision(TipoEmision.NORMAL.getCodigo());

        /*
         * Configuracion de status del emiosor
         */
        emisor.setStatus(Status.ONLINE);

        /*
         * Configuracion del plan contratado
         */
        if (emisor.getFkPlan() == null || emisor.getFkPlan().getIdPlan() <= 0) {
            // Elige el plan gratuito que tiene por defecto que esta en el id 1
            Optional<Plan> optionalPlan = planRepository.findById(1);

            emisor.setFkPlan(Plan.builder().idPlan(optionalPlan.get().getIdPlan()).build());
            /*
             * Configuracion de la cantidad contratada
             */
            emisor.setCantidadContratada(optionalPlan.get().getCantidad());
            /*
             * Configuracion de la cantidad usada
             */
            emisor.setCantidadUsada(0);
            /*
             * Configuracion de la fecha de inicio
             */
            emisor.setFechaInicio(new Fecha().fechaCreacion());
            /*
             * Configuracion de la fecha que finaliza los comprobantes
             */
            emisor.setFechaFin(new Fecha().addMeses(optional.get().getFechaInicio(),
                    Integer.parseInt(optionalPlan.get().getPeriodo())));
        } else {
            // si es elegido un plan se agrega los campos correspondientes
            Optional<Plan> optionalPlan = planRepository.findById(emisor.getFkPlan().getIdPlan());

            emisor.setFkPlan(optionalPlan.get());

            emisor.setFechaInicio(new Fecha().fechaCreacion());
            /*
             * Configuracion de la fecha que finaliza los comprobantes
             */
            emisor.setFechaFin(new Fecha().addMeses(optional.get().getFechaInicio(),
                    Integer.parseInt(optionalPlan.get().getPeriodo())));
            /*
             * Configuracion de la cantidad contratada
             */
            emisor.setCantidadContratada(optionalPlan.get().getCantidad());
            /*
             * Configuracion de la cantidad usada
             */
            emisor.setCantidadUsada(0);
            /*
             * Configuracion de la fecha de inicio
             */
            emisor.setFechaInicio(new Fecha().fechaCreacion());
            /*
             * Configuracion de la fecha que finaliza los comprobantes
             */
            emisor.setFechaFin(new Fecha().addMeses(optional.get().getFechaInicio(),
                    Integer.parseInt(optionalPlan.get().getPeriodo())));
        }

        /*
         * Configuracion de la fecha que se registro
         */
        emisor.setCreated(new Fecha().fechaCreacion());

        /*
         * Se debe enviar el mail al usuario creado
         */
        repository.save(emisor);

        SignUpEmisorDto user = SignUpEmisorDto.builder()
                .username(emisor.getRuc())
                .password(password.toCharArray())
                .email(emisor.getCorreoRemitente())
                .nombres(nombres)
                .apellidos(apellidos)
                .build();

        /*
         * Nos comunicamos con el servicio que genera el usuario
         */
        Respuesta response = userService.register(user);

        /*
         * si no se guarda o genera un error los datos se eliminan
         */
        if (response.getType() == RespuestaType.WARNING) {
            // se elimina el registro de la base de datos para que se pueda reiniciar el registro con otra solicitud
            repository.deleteById(repository.findByRuc(emisor.getRuc()).get().getIdEmisor());
            return response;
        }

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();
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
