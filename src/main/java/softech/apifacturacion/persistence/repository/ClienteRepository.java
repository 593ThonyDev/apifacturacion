package softech.apifacturacion.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import softech.apifacturacion.persistence.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}

