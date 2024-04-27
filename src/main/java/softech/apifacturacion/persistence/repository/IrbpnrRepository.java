package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Irbpnr;

public interface IrbpnrRepository extends JpaRepository<Irbpnr, Integer> {
    
}
