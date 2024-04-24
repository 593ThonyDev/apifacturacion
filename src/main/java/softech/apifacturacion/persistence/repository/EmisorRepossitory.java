package softech.apifacturacion.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Emisor;

public interface EmisorRepossitory extends JpaRepository<Emisor, Integer>{
    Optional<Emisor> findByRuc(String ruc);
}
