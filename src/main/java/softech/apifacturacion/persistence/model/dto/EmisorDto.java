package softech.apifacturacion.persistence.model.dto;

import java.time.ZonedDateTime;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Plan;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmisorDto {

    Integer idEmisor;
    Plan fkPlan;
    String ruc;
    String tipoEmision;
    String razonSocial;
    String nombreComercial;
    String direccionMatriz;
    String contribuyenteEspecial;
    String obligadoContabilidad;
    String agenteRetencion;
    String tipoContribuyente;
    String regimenMicroempresa;
    String passFirma;
    String logo;
    String correoRemitente;
    Integer cantidadContratada;
    Integer cantidadUsada;
    ZonedDateTime created;
    Status status;
}