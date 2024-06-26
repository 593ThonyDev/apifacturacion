package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

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
    @JoinColumn(name = "fkestablecimiento")
    Establecimiento fkEstablecimiento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkcajero")
    Cajero fkCajero;

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
    @Column(name = "secuencialiquidacion")
    String secuenciaLiquidacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialcredito")
    String secuencialCredito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialdebito")
    String secuencialDebito;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialremision")
    String secuencialRemision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencialretencion")
    String secuencialRetencion;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

}
