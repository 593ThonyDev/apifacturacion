package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstablecimientoPageDto {

    private Integer idEstablecimiento;
    private EmisorDto fkEmisor;
    private String nombre;
    private String codigo;
    private String web;
    private String nombreComercial;
    private String direccion;
    private String email;
    private Status status;
    private String logo;
    private String proformaSecuencia;
}
