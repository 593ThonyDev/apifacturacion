package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;

import softech.apifacturacion.persistence.model.Plan;
import softech.apifacturacion.response.Respuesta;

public interface PlanService {

    Respuesta save(Plan plan);

    Respuesta update(Plan plan);

    Page<Plan> getAll(Pageable pageable);

}
