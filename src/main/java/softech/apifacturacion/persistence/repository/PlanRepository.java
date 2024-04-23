package softech.apifacturacion.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    Optional<Plan> findByNombre(String nombre);

}
