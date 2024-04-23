package softech.apifacturacion.persistence.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.persistence.repository.PlanRepository;
import softech.apifacturacion.persistence.service.PlanService;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    @Autowired
    private final PlanRepository repository;

    @Override
    public Respuesta save(Plan plan) {

        Optional<Plan> optional = repository.findByNombre(plan.getNombre());

        if (optional.isPresent()) {
            return Respuesta.builder()
                    .message("Cambie de nombre del plan ocupado")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        if (plan.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre del plan")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getCantidad() <= 0) {
            return Respuesta.builder()
                    .message("Agregue la cantidad del comprobantes")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getPrecio() < 0) {
            return Respuesta.builder()
                    .message("No se debe agregar precios negativos")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getDescripcion().isEmpty()) {
            return Respuesta.builder()
                    .message("Debe agregar una descripcion")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getPeriodo().isEmpty()) {
            return Respuesta.builder()
                    .message("Debe agregar un periodo correcto")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        repository.save(plan);

        return Respuesta.builder()
                .message("Registro guardado con exito")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta update(Plan plan) {

        Optional<Plan> optional = repository.findById(plan.getIdPlan());

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el registro")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        if (plan.getIdPlan() <= 0 || plan.getIdPlan() == null) {
            return Respuesta.builder()
                    .message("Agregue el nombre del plan")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .message("Agregue el nombre del plan")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getCantidad() <= 0) {
            return Respuesta.builder()
                    .message("Agregue la cantidad del comprobantes")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getPrecio() < 0) {
            return Respuesta.builder()
                    .message("No se debe agregar precios negativos")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getDescripcion().isEmpty()) {
            return Respuesta.builder()
                    .message("Debe agregar una descripcion")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getPeriodo().isEmpty()) {
            return Respuesta.builder()
                    .message("Debe agregar un periodo correcto")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        if (plan.getStatus() == null) {
            return Respuesta.builder()
                    .message("Debe seleccionar un status")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        repository.save(plan);

        return Respuesta.builder()
                .message("Cambios guardados con exito")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Page<Plan> getAll(Pageable pageable) {
        Page<Plan> pagina = repository.findAll(pageable);
        if (pagina.isEmpty()) {
            return null;
        }
        return pagina;
    }
}
