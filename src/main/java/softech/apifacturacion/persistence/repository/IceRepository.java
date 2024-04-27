package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Ice;

public interface IceRepository extends JpaRepository<Ice, Integer> {
    
}
