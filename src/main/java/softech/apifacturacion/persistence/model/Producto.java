package softech.apifacturacion.persistence.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import softech.apifacturacion.persistence.enums.ProductSubcidio;
import softech.apifacturacion.persistence.enums.ProductTipo;
import softech.apifacturacion.persistence.enums.Status;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idproducto")
    Integer idProducto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkemisor")
    Emisor fkEmisor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkiva")
    Iva fkIva;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkice")
    Ice fkIce;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkirbpnr")
    Irbpnr fkIrbpnr;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombre")
    String nombre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "stock")
    Integer stock;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "preciounitario")
    Double precioUnitario;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "codprincipal")
    String codPrincipal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "codauxiliar")
    String codAuxiliar;

    @Column(name = "subcidio")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    ProductSubcidio subcidio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "subsidiovalor")
    Double subsidioValor;

    @Column(name = "subsidio")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    ProductTipo tipo;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "img1")
    String img1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "img2")
    String img2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "img3")
    String img3;

}
