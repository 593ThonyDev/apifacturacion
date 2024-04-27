package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
}
