package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import softech.apifacturacion.status.Status;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "establecimiento")
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idestablecimiento ")
    Integer idEstablecimiento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkemisor")
    Emisor fkEmisor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombre")
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "codigo")
    String codigo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "web")
    String web;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombrecomercial")
    String nombreComercial;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "direccion")
    String direccion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "email")
    String email;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "logo")
    String logo;

}
