package softech.apifacturacion.persistence.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import softech.apifacturacion.status.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idplan ")
    Integer idPlan;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombre")
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "cantidad")
    Integer cantidad;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "precio")
    Double precio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "periodo")
    String periodo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "descripcion")
    String descripcion;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

}
