package softech.apifacturacion.persistence.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idcliente")
    Integer idCliente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkemisor")
    Emisor fkEmisor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombre")
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "tipoidentificacion")
    String tipoIdentificacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "identificacion")
    String Identificacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "direccion")
    String direccion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "email")
    String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "telefono")
    String telefono;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created")
    ZonedDateTime created;

}
