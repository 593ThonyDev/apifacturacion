package softech.apifacturacion.persistence.model.dto;

import lombok.*;
import softech.apifacturacion.persistence.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstablecimientoDto {

    private Integer idEstablecimiento;
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
