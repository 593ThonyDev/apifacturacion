package softech.apifacturacion.persistence.service;

import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.response.Respuesta;

public interface EmisorService {

    public Respuesta registerEmisor(Emisor emisor, String nombres, String apellidos);

}
