package softech.apifacturacion.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.DetalleFactura;
import softech.apifacturacion.persistence.model.Factura;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Integer> {
    List<DetalleFactura> findByFkFactura(Factura factura);
}
