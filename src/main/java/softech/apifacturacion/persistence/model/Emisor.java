package softech.apifacturacion.persistence.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emisor")
public class Emisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "idemisor")
    Integer idEmisor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "fkplan")
    Plan fkPlan;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ruc")
    String ruc;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ambiente")
    String ambiente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "tipoemision")
    String tipoEmision;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "razonsocial")
    String razonSocial;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "nombrecomercial")
    String nombreComercial;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "direccionmatriz")
    String direccionMatriz;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "contribuyenteespecial")
    String contribuyenteEspecial;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "obligadocontabilidad")
    String obligadoContabilidad;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "agenteretencion")
    String agenteRetencion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "tipocontribuyente")
    String tipoContribuyente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "regimenmicroempresa")
    String regimenMicroempresa;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "dirlogo")
    String dirLogo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "dirfirma")
    String dirFirma;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "dirautorizados")
    String dirAutorizados;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "passfirma")
    String passFirma;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "servercorreo")
    String serverCorreo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "correoremitente")
    String correoRemitente;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "passcorreo")
    String passCorreo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "puerto")
    Integer puerto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ssl")
    String ssl;

    @Column(name = "status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Enumerated(EnumType.STRING)
    Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "fechainicio")
    ZonedDateTime fechaInicio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "fechafin")
    ZonedDateTime fechaFin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "cantcontratada")
    Integer cantidadContratada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "cantusada")
    Integer cantidadUsada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created")
    ZonedDateTime created;

}
