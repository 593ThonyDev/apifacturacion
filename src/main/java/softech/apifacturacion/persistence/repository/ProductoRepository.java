package softech.apifacturacion.persistence.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT e FROM Producto e WHERE e.nombre LIKE ?1 OR e.codPrincipal LIKE ?2")
    List<Producto> findByPartialNombreOrPartialCodPrincipal(String name, String description);

    Page<Producto> findByFkEmisor(Emisor emisor, Pageable pageable);

}
