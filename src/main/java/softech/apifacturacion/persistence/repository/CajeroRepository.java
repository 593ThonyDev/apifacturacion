package softech.apifacturacion.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Cajero;

public interface CajeroRepository extends JpaRepository<Cajero, Integer> {

    Optional<Cajero> findByIdentificacion(String identificacion);

}
