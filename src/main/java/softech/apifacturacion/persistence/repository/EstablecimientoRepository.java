package softech.apifacturacion.persistence.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.*;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {
    List<Establecimiento> findByFkEmisor(Emisor emisor);
}
