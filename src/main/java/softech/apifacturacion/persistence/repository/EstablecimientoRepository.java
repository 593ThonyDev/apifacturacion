package softech.apifacturacion.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Establecimiento;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {
    List<Establecimiento> findByFkEmisor(Emisor emisor);
}
