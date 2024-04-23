package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import ch.qos.logback.core.status.Status;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ptoemision")
public class PtoEmision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idptoemision")
    Integer idPtoemision;

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
    @Column(name = "secuenciafactura")
    String secuenciaFactura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuenciaLiquidacion")
    String secuenciaLiquidacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialCredito")
    String secuencialCredito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialDebito")
    String secuencialDebito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialRemision")
    String secuencialRemision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialRetencion")
    String secuencialRetencion;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

}
