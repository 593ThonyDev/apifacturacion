package softech.apifacturacion.persistence.controller;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.*;
import softech.apifacturacion.persistence.service.ProductoService;
import softech.apifacturacion.response.*;

@RestController
@RequestMapping("/facturacion/v1/producto")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService service;

    @PostMapping("/save")
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
            @RequestParam("img1") MultipartFile img1,
            @RequestParam("img2") MultipartFile img2,
            @RequestParam("img3") MultipartFile img3) {

        try {

            Producto producto = Producto.builder()
                    .nombre(nombre)
                    .stock(Integer.parseInt(stock))
                    .codPrincipal(codprincipal)
                    .codAuxiliar(codauxiliar)
                    .precioUnitario(Double.parseDouble(preciounitario))
                    .subcidio(ProductSubcidio.valueOf(subcidio.isEmpty() ? "OFFLINE" : subcidio))
                    .subcidioValor(Double.parseDouble(subcidiovalor.isEmpty() ? "0.0" : subcidiovalor))
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
                    .message("Error interno en el servidor al guardar el producto " + e)
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
    public ResponseEntity<Page<ProductoPageDto>> getAll(Pageable pageable) {
        try {
            Page<ProductoPageDto> pagina = service.getAll(pageable);
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

    @GetMapping("/getDataByRuc/{ruc}")
    public ResponseEntity<Page<ProductoByRucPageDto>> getAllByRuc(@PathVariable("ruc") String ruc, Pageable pageable) {
        try {
            Page<ProductoByRucPageDto> pagina = service.getAllByRuc(ruc, pageable);
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

    @GetMapping("/getInfo/{idProducto}")
    public ResponseEntity<Respuesta> getDataByRuc(@PathVariable("idProducto") String idProducto) {
        try {
            Respuesta response = service.getbyId(Integer.parseInt(idProducto));
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

    @GetMapping("/search/{ruc}/{searchTerm}")
    public ResponseEntity<List<ProductoByRucPageDto>> searchProductByRuc(
            @PathVariable("ruc") String ruc,
            @PathVariable("searchTerm") String searchTerm) {

        List<ProductoByRucPageDto> lista = service.searchByRuc(ruc, searchTerm);

        if (lista != null && lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else if (lista != null) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<ProductoPageDto>> searchProduct(@PathVariable("searchTerm") String searchTerm) {
       
        List<ProductoPageDto> lista = service.search(searchTerm);
       
        if (lista != null && lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else if (lista != null) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
