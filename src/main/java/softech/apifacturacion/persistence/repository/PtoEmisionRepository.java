package softech.apifacturacion.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Cajero;
import softech.apifacturacion.persistence.model.PtoEmision;

public interface PtoEmisionRepository extends JpaRepository<PtoEmision, Integer> {
    Optional<PtoEmision> findByFkCajero(Cajero cajero);
}
