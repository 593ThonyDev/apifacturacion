package softech.apifacturacion.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Iva;

public interface IvaRepository extends JpaRepository<Iva, Integer> {

}
