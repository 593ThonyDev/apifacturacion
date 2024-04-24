package softech.apifacturacion.persistence.service;

import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.response.Respuesta;

public interface EmisorService {

    public Respuesta registerEmisor(Emisor emisor, String nombres, String apellidos, String password);

    public Respuesta updateEmisor(Emisor emisor, MultipartFile logo, MultipartFile firma);

}
