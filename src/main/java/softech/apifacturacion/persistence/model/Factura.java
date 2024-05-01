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
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idfactura")
    Integer idFactura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkptoemision")
    PtoEmision fkPtoEmision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkestablecimiento")
    Establecimiento fkEstablecimiento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkcliente")
    Cliente fkCliente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "claveacceso")
    String claveAcceso;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "numautorizacion")
    String numAutorizacion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "fechaautorizacion")
    ZonedDateTime fechaAutorizacion;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ambiente")
    String ambiente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "tipoemision")
    String tipoEmision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "secuencial")
    String secuencial;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "formapago")
    String formaPago;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "plazo")
    String plazo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "fechaemision")
    ZonedDateTime fechaemision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombrearchivo")
    String nombreArchivo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "totalsinimpuestos")
    Double totalSinImpuestos;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subtotal")
    Double subTotal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subtotal0")
    Double subTotal0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subtotalnoiva")
    Double subTotalNoIva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subtotalexcentoiva")
    Double subTotalExcentoIva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "valorice")
    Double valorIce;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "valorirbpn")
    Double valorIrbpn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "iva")
    Double iva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "totaldescuento")
    Double totaldescuento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "propina")
    Double propina;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "valortotal")
    Double valorTotal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "totalsubsidio")
    Double totalSubsidio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subsidiosiniva")
    Double totalSubsidioSinIva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "firmado")
    Integer firmado;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "observacion")
    String observacion;

}
