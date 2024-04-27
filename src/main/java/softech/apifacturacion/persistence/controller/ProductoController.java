package softech.apifacturacion.persistence.controller;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.ProductSubcidio;
import softech.apifacturacion.persistence.enums.ProductTipo;
import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Producto;
import softech.apifacturacion.persistence.service.ProductoService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v/producto")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService service;

    @PatchMapping("/save")
    public ResponseEntity<Respuesta> save(
            @RequestParam("ruc") String ruc,
            @RequestParam("nombre") String nombre,
            @RequestParam("stock") String stock,
            @RequestParam("preciounitario") String preciounitario,
            @RequestParam("codprincipal") String codprincipal,
            @RequestParam("codauxiliar") String codauxiliar,
            @RequestParam("subcidio") String subcidio,
            @RequestParam("subcidiovalor") String subcidiovalor,
            @RequestParam("tipo") String tipo,
            @RequestParam("status") String status,
            @RequestParam("img1") MultipartFile img1,
            @RequestParam("img2") MultipartFile img2,
            @RequestParam("img3") MultipartFile img3) {

        try {
            Producto producto = Producto.builder()
                    .nombre(nombre)
                    .stock(Integer.parseInt(stock))
                    .precioUnitario(Double.parseDouble(preciounitario))
                    .codPrincipal(codprincipal)
                    .codAuxiliar(codauxiliar)
                    .subcidio(ProductSubcidio.valueOf(subcidio))
                    .subsidioValor(Double.parseDouble(subcidiovalor))
                    .tipo(ProductTipo.valueOf(tipo))
                    .build();

            Respuesta response = service.save(producto, ruc, img1, img2, img3);

            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en la cofiguracion del producto: " + e.getMessage());
            return ResponseEntity.badRequest().body(Respuesta.builder()
                    .message("Error interno en el servidor en la cofiguracion del producto")
                    .build());
        }
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<Respuesta> changeStatus(@RequestParam("idProducto") String idProducto,
            @RequestParam("status") String status) {
        try {
            Respuesta response = service.changeStatus(Integer.parseInt(idProducto), Status.valueOf(status));
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en actualizar el status producto: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en actualizar el status producto: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/getData")
    public ResponseEntity<Page<Producto>> getAll(Pageable pageable) {
        try {
            Page<Producto> pagina = service.getAll(pageable);
            if (pagina != null && pagina.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else if (pagina != null) {
                return ResponseEntity.ok(pagina);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage().toString());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/getData/{idProducto}")
    public ResponseEntity<Respuesta> getDataByRuc(@PathVariable("idProducto") String idProducto) {
        try {
            Respuesta response = service.getbyid(Integer.parseInt(idProducto));
            if (response.getType() == RespuestaType.SUCCESS) {
                return ResponseEntity.ok().body(Respuesta.builder()
                        .content(response.getContent())
                        .build());
            } else {
                logger.error(response.getMessage());
                return ResponseEntity.badRequest().body(Respuesta.builder()
                        .message(response.getMessage())
                        .build());
            }
        } catch (Exception e) {
            logger.error("Error interno en el servidor en listar al producto: " + e.getCause());
            return ResponseEntity.internalServerError().body(Respuesta.builder()
                    .message("Error interno en el servidor en listar al producto: " + e.getMessage())
                    .build());
        }
    }


    @PatchMapping("/updateImage")
    public ResponseEntity<Respuesta> updateImage(
            @RequestParam("idProducto") String idProduct,
            @RequestParam(name = "img1", required = false) MultipartFile img1,
            @RequestParam(name = "img2", required = false) MultipartFile img2,
            @RequestParam(name = "img3", required = false) MultipartFile img3) {

        Respuesta response = service.updateImage(Integer.parseInt(idProduct), img1, img2, img3);

        if (response.getType() == RespuestaType.SUCCESS) {
            return ResponseEntity.ok(Respuesta.builder()
                    .message(response.getMessage())
                    .build());
        } else {
            return ResponseEntity.badRequest()
                    .body(Respuesta.builder().type(response.getType())
                            .message(response.getMessage())
                            .build());
        }
    }

    
}
