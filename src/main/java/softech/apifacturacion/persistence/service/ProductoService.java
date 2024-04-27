package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Producto;
import softech.apifacturacion.response.Respuesta;

public interface ProductoService {

    Page<Producto> getAll(Pageable pageable);

    Respuesta save(Producto producto ,String ruc ,MultipartFile img1 ,MultipartFile img2, MultipartFile img3);

    Respuesta update(Producto Producto );

    Respuesta changeStatus(Integer idProducto, Status status);

    Respuesta updateImage(Integer idProduct, MultipartFile img1, MultipartFile img2, MultipartFile img3);

    Respuesta getbyid(Integer idProduct);

}