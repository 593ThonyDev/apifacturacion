package softech.apifacturacion.persistence.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cajero")
public class Cajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idcajero")
    Integer idCajero;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "identificacion")
    String identificacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombres")
    String nombres;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "apellidos")
    String apellidos;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "email")
    String email;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created")
    ZonedDateTime created;

}
