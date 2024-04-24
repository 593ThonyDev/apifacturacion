package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.enums.TipoEmision;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.repository.EmisorRepossitory;
import softech.apifacturacion.persistence.repository.PlanRepository;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.response.*;
import softech.apifacturacion.upload.UploadImage;
import softech.apifacturacion.upload.UploadSignature;

public class EmisorServiceImpl implements EmisorService {

    @Autowired
    EmisorRepossitory repository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    UploadImage imageUpload;

    @Autowired
    UploadSignature signatureUpload;

    @Override
    public Respuesta registerEmisor(Emisor emisor, String nombres, String apellidos) {

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

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Regsitro guardado correctamente")
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

        }
        if (emisor.getNombreComercial().isEmpty()) {

        }
        if (emisor.getDireccionMatriz().isEmpty()) {

        }

        if (emisor.getObligadoContabilidad().isEmpty()) {

        }
        if (emisor.getAgenteRetencion().isEmpty()) {

        }
        if (emisor.getTipoContribuyente().isEmpty()) {

        }
        if (logo.isEmpty()) {

        }
        if (firma.isEmpty()) {

        }

        optional.get().setDirLogo(imageUpload.addImage(optional.get().getRuc(), "logoEmisor", logo));
        optional.get()
                .setDirFirma(signatureUpload.addSignature(optional.get().getRuc(), optional.get().getRuc(), firma));

        /*
         * Se debe enviar el mail al usuario los datos de la empresa que ha sido
         * actualizado
         */

        repository.save(optional.get());
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Regsitro actualizado correctamente")
                .build();
    }

}
