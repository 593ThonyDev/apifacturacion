package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;

import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.response.Respuesta;

public interface FacturaService {

    Respuesta generateClaveAcceso(String ruc, PtoEmision ptoEmision);

    Respuesta save(Factura factura);

    Respuesta update(Factura factura);

    Boolean createXML(String ruc, String cliente, String claveAcceso);

    Boolean firmarXML(String ruc, String cliente, String claveAcceso);

    Respuesta enviarSRI(String claveAcceso);

    Respuesta anularSRI(String claveAcceso);

    Respuesta deleteFactura(String ruc, String claveAcceso);

    Page<Factura> getByRuc(String ruc, Pageable pageable);

    Page<Factura> getAll(Pageable pageable);

}
