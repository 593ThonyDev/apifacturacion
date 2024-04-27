package softech.apifacturacion.persistence.service.implementation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.model.dto.ProductoByRucPageDto;
import softech.apifacturacion.persistence.model.dto.ProductoPageDto;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.ProductoService;
import softech.apifacturacion.response.*;
import softech.apifacturacion.upload.UploadImage;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private final ProductoRepository repository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final EmisorRepossitory emisorRepository;

    @Autowired
    private UploadImage uploadImage;

    @Override
    public Page<ProductoPageDto> getAll(Pageable pageable) {
        Page<Producto> pagina = repository.findAll(pageable);
        if (!pagina.isEmpty()) {
            return pagina.map(product -> modelMapper.map(product, ProductoPageDto.class));
        } else {
            return Page.empty();
        }
    }

    @Override
    public Page<ProductoByRucPageDto> getAllByRuc(String ruc, Pageable pageable) {
        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return null;
        }
        Page<Producto> pagina = repository.findByFkEmisor(optional.get(), pageable);
        if (!pagina.isEmpty()) {
            return pagina.map(product -> modelMapper.map(product, ProductoByRucPageDto.class));
        } else {
            return Page.empty();
        }
    }

    @Override
    public Respuesta save(Producto producto, String ruc, MultipartFile img1, MultipartFile img2, MultipartFile img3) {

        Optional<Emisor> optional = emisorRepository.findByRuc(ruc);
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el ruc indicado")
                    .build();
        }
        if (producto.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre de producto")
                    .build();
        }
        if (producto.getStock() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un valor de stock mayor a cero")
                    .build();
        }
        if (producto.getPrecioUnitario() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agrega un precio unitario mayor a cero")
                    .build();
        }
        if (producto.getCodPrincipal().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el codigo principal del producto")
                    .build();
        }
        if (producto.getCodAuxiliar().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el codigo auxiliar del producto")
                    .build();
        }
        if (producto.getSubcidio() == null) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe seleccionar un tipo de subcidio")
                    .build();
        }

        if (producto.getSubcidio() == ProductSubcidio.ONLINE) {
            if (producto.getSubcidioValor() <= 0) {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("El valor del subcidio debe ser mayor a cero")
                        .build();
            }
        } else {
            producto.setSubcidio(ProductSubcidio.OFFLINE);
            producto.setSubcidioValor(0.0);
        }
        if (producto.getTipo() == ProductTipo.BIEN) {
            producto.setTipo(ProductTipo.BIEN);
        } else {
            producto.setTipo(ProductTipo.SERVICIO);
        }
        producto.setStatus(Status.ONLINE);
        if (img1.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la imagen uno al producto")
                    .build();
        }
        if (img2.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la imagen dos al producto")
                    .build();
        }
        if (img2.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la imagen tres al producto")
                    .build();
        }

        producto.setImg1(uploadImage.addImage(optional.get().getRuc() + "/productos",
                producto.getCodPrincipal().replace(" ", "") + "-" + producto.getNombre().replace(" ", "") + "img1",
                img1));

        producto.setImg2(uploadImage.addImage(optional.get().getRuc() + "/productos",
                producto.getCodPrincipal().replace(" ", "") + "-" + producto.getNombre().replace(" ", "") + "img2",
                img2));

        producto.setImg3(uploadImage.addImage(optional.get().getRuc() + "/productos",
                producto.getCodPrincipal().replace(" ", "") + "-" + producto.getNombre().replace(" ", "") + "img3",
                img3));

        /*
         * Configuracion del emisor
         */
        producto.setFkEmisor(optional.get());

        repository.save(producto);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Registro guardado correctamente")
                .build();

    }

    @Override
    public Respuesta update(Producto producto) {

        Optional<Producto> optional = repository.findById(producto.getIdProducto());
        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el producto indicado")
                    .build();
        }
        if (producto.getNombre().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un nombre de producto")
                    .build();
        }
        if (producto.getStock() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un valor de stock mayor a cero")
                    .build();
        }
        if (producto.getPrecioUnitario() <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agrega un precio unitario mayor a cero")
                    .build();
        }
        if (producto.getCodPrincipal().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el codigo principal del producto")
                    .build();
        }
        if (producto.getCodAuxiliar().isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar el codigo auxiliar del producto")
                    .build();
        }
        if (producto.getSubcidio() == ProductSubcidio.ONLINE || producto.getSubcidio() != null) {
            producto.setSubcidio(ProductSubcidio.ONLINE);

            if (producto.getSubcidioValor() <= 0) {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("Debe agregar un valor del subcidio debe ser mayor a cero")
                        .build();
            }

        } else {
            producto.setSubcidio(ProductSubcidio.OFFLINE);
            producto.setSubcidioValor(null);
        }

        repository.save(producto);
        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("Producto actualizado con exito")
                .build();

    }

    @Override
    public Respuesta changeStatus(Integer idProducto, Status status) {

        Optional<Producto> optional = repository.findById(idProducto);

        if (!optional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el registro")
                    .build();
        }

        if (status == Status.OFFLINE) {
            optional.get().setStatus(Status.OFFLINE);
        } else {
            optional.get().setStatus(Status.ONLINE);
        }

        repository.save(optional.get());

        return Respuesta.builder()
                .message("Registro actualizado correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

    @Override
    public Respuesta updateImage(Integer idProduct, MultipartFile img1, MultipartFile img2, MultipartFile img3) {
        Optional<Producto> optionalProduct = repository.findById(idProduct);

        if (!optionalProduct.isPresent()) {
            return Respuesta.builder()
                    .message("No se encontró el producto con el ID especificado")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        Producto product = optionalProduct.get();

        try {
            if (img1 != null && img2 == null && img3 == null) {
                uploadImage.deleteImage(optionalProduct.get().getFkEmisor().getRuc() + "/productos", product.getImg1());
                return processImage(img1, product, Producto::getImg1, Producto::setImg1);
            } else if (img2 != null && img1 == null && img3 == null) {
                uploadImage.deleteImage(optionalProduct.get().getFkEmisor().getRuc() + "/productos", product.getImg2());
                return processImage(img2, product, Producto::getImg2, Producto::setImg2);
            } else if (img3 != null && img1 == null && img2 == null) {
                uploadImage.deleteImage(optionalProduct.get().getFkEmisor().getRuc() + "/productos", product.getImg3());
                return processImage(img3, product, Producto::getImg3, Producto::setImg3);
            } else {
                return Respuesta.builder()
                        .message("Debe agregar una sola imagen a la vez")
                        .type(RespuestaType.WARNING)
                        .build();
            }
        } catch (IOException e) {
            return Respuesta.builder()
                    .message("Hubo un error al guardar la imagen: " + e.getMessage())
                    .type(RespuestaType.WARNING)
                    .build();
        }
    }

    private Respuesta processImage(MultipartFile image, Producto producto,
            Function<Producto, String> getImage,
            BiConsumer<Producto, String> setImage) throws IOException {

        String newImage = uploadImage.addImage(producto.getFkEmisor().getRuc() + "/productos",
                producto.getNombre().replace(" ", "-") + "-" + UUID.randomUUID().toString().substring(0, 8), image);
        setImage.accept(producto, newImage);

        repository.save(producto);

        return Respuesta.builder()
                .message("Imagen actualizada con éxito")
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public Respuesta getbyId(Integer idProduct) {
        Optional<Producto> optionalProduct = repository.findById(idProduct);
        if (!optionalProduct.isPresent()) {
            return Respuesta.builder()
                    .message("No existe el producto")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        return Respuesta.builder()
                .content(new Object[] { optionalProduct.get() })
                .type(RespuestaType.SUCCESS)
                .build();
    }

    @Override
    public List<ProductoByRucPageDto> search(String searchTerm) {
        String searchValue = "%" + searchTerm + "%";
        List<Producto> pagina = repository.findByPartialNombreOrPartialCodPrincipal(searchValue, searchValue);
        if (!pagina.isEmpty()) {
            return pagina.stream()
                    .map(product -> modelMapper.map(product, ProductoByRucPageDto.class))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}