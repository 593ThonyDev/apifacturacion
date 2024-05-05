package softech.apifacturacion.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Cliente;
import softech.apifacturacion.persistence.model.Emisor;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    Page<Cliente> findByFkEmisor(Emisor emisor, Pageable pageable);

    List<Cliente> findByIdentificacion(String identificacion);

    Optional<Cliente> findByIdentificacionAndFkEmisor(String identificacion, Emisor emisor);

}
