package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.enums.TipoEmision;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.repository.EmisorRepossitory;
import softech.apifacturacion.persistence.repository.PlanRepository;
import softech.apifacturacion.persistence.service.EmisorService;
import softech.apifacturacion.response.*;

public class EmisorServiceImpl implements EmisorService {

    @Autowired
    EmisorRepossitory repository;

    @Autowired
    PlanRepository planRepository;

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
         * Configuracion ambiente para la emision de los comprobantes
         */
        emisor.setTipoEmision(TipoEmision.NORMAL.getCodigo());

        /*
         * Configuracion del plan contratado
         */
        if (emisor.getFkPlan() == null || emisor.getFkPlan().getIdPlan() <= 0) {
            // Elige el plan gratuito que tiene por defecto que esta en el id1
            Optional<Plan> optionalPlan = planRepository.findById(1);
            emisor.setFkPlan(Plan.builder().idPlan(optionalPlan.get().getIdPlan()).build());
            emisor.setCantidadContratada(optionalPlan.get().getCantidad());
            emisor.setCantidadUsada(0);
            emisor.setFechaInicio(new Fecha().fechaCreacion());
            emisor.setFechaFin(new Fecha().addMeses(optional.get().getFechaInicio(),
                    Integer.parseInt(optionalPlan.get().getPeriodo())));
        } else {
            // si es elegido un plan se agrega los campos correspondientes
            Optional<Plan> optionalPlan = planRepository.findById(emisor.getFkPlan().getIdPlan());
            emisor.setFkPlan(optionalPlan.get());
            emisor.setCantidadContratada(optionalPlan.get().getCantidad());
            emisor.setCantidadUsada(0);
            emisor.setFechaInicio(new Fecha().fechaCreacion());
            emisor.setFechaFin(new Fecha().addMeses(optional.get().getFechaInicio(),
                    Integer.parseInt(optionalPlan.get().getPeriodo())));
        }

        emisor.setTipoEmision("1");
        emisor.setStatus(Status.ONLINE);

        return null;
    }

}
