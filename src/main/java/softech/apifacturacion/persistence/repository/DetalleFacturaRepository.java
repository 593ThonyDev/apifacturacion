package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.DetalleFactura;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Integer> {

}
