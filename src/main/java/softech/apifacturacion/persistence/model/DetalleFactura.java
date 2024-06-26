package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detallefactura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "iddetallefactura")
    Integer idDetalleFactura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkproducto")
    Producto fkProducto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkfactura")
    Factura fkFactura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "cantidad")
    Integer cantidad;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombre")
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "codigo")
    String codigo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "preciounitario")
    Double precioUnitario;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "descuento")
    Double descuento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subtotal")
    Double subTotal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "iva")
    Double iva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ice")
    Double ice;

}
