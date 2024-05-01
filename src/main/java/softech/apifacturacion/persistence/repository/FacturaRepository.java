package softech.apifacturacion.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    
    Optional<Factura> findByClaveAcceso(String claveAcceso);

}
