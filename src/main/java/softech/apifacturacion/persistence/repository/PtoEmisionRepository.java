package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.PtoEmision;

public interface PtoEmisionRepository extends JpaRepository<PtoEmision, Integer> {

}
