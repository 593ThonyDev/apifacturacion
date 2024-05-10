package softech.apifacturacion.persistence.service.implementation;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.function.Fecha;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.*;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private final CajeroRepository cajeroRepository;

    @Autowired
    private final FacturaRepository facturaRepository;

    @Autowired
    private final PtoEmisionRepository ptoEmisionRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final ProductoRepository productoRepository;

    @Autowired
    private final DetalleFacturaRepository detalleFacturaRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public Respuesta save(String ruc, String cajIdentificacion, String cliIdentificacion, Factura factura,
            DetalleFactura[] detalleFacturas) {

        /*
         * VALIDAR LOS CAMPOS DE LA FACTURA SI ESTÁN PRESENTES
         */
        if (ruc.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar un RUC en la factura")
                    .build();
        }
        if (cajIdentificacion.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la identificación del cajero")
                    .build();
        }
        if (cliIdentificacion.isEmpty()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar la identificación del cliente")
                    .build();
        }
        if (factura == null) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el objeto de factura")
                    .build();
        }
        if (detalleFacturas.length <= 0) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Debe agregar productos en el detalle de la factura")
                    .build();
        }

        // CONFIGURAR LA FORMA DE PAGO VÁLIDA
        switch (factura.getFormaPago()) {
            case "SIN_SISTEMA_FINANCIERO":
                factura.setFormaPago(FormaPago.SIN_SISTEMA_FINANCIERO.getCodigo());
                break;
            case "COMPESACION_DEUDA":
                factura.setFormaPago(FormaPago.COMPESACION_DEUDA.getCodigo());
                break;
            case "TARJETA_DEBITO":
                factura.setFormaPago(FormaPago.TARJETA_DEBITO.getCodigo());
                break;
            case "DINERO_ELECTRONICO":
                factura.setFormaPago(FormaPago.DINERO_ELECTRONICO.getCodigo());
                break;
            case "TARJETA_PREPAGO":
                factura.setFormaPago(FormaPago.TARJETA_PREPAGO.getCodigo());
                break;
            case "TARJETA_CREDITO":
                factura.setFormaPago(FormaPago.TARJETA_CREDITO.getCodigo());
                break;
            case "CON_SISTEMA_FINANCIERO":
                factura.setFormaPago(FormaPago.CON_SISTEMA_FINANCIERO.getCodigo());
                break;
            case "ENDOSO_TITULOS":
                factura.setFormaPago(FormaPago.ENDOSO_TITULOS.getCodigo());
                break;
            default:
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("Forma de pago desconocida, agregue una válida")
                        .build();
        }

        // VALIDAR SI EL CAJERO ESTÁ REGISTRADO
        Optional<Cajero> cajeroOptional = cajeroRepository.findByIdentificacion(cajIdentificacion);
        if (!cajeroOptional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el punto de emisión indicado")
                    .build();
        }

        /*
         * OBTENER EL PUNTO DE EMISIÓN POR MEDIO DEL CAJERO
         * VALIDAR SI EL CAJERO Y PUNTO DE EMISIÓN ESTÁN ACTIVOS O NO
         */
        Optional<PtoEmision> ptoOptional = ptoEmisionRepository.findByFkCajero(cajeroOptional.get());
        if (ptoOptional.get().getStatus() == Status.OFFLINE) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("El punto de emisión está de baja, actívalo o intenta con otro que se encuentre activo")
                    .build();
        }

        // VALIDAR SI EL RUC DEL PUNTO DE EMISIÓN CORRESPONDE AL QUE VIENE DEL SISTEMA
        if (!ptoOptional.get().getFkEstablecimiento().getFkEmisor().getRuc().equals(ruc)) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("El RUC no corresponde al punto de emisión, verifícalo")
                    .build();
        }

        // VALIDAR EL CLIENTE SI EXISTE Y TAMBIÉN SI CORRESPONDE AL EMISOR
        Optional<Cliente> clienteOptional = clienteRepository.findByIdentificacionAndFkEmisor(cliIdentificacion,
                ptoOptional.get().getFkEstablecimiento().getFkEmisor());
        if (!clienteOptional.isPresent()) {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("No existe el cliente, regístralo y después vuelve a guardar la factura")
                    .build();
        }

        /*
         * CONFIGURAR LA FACTURA
         */
        // AGREGAR FECHA EMISION
        factura.setFechaemision(new Fecha().fechaCreacion());
        // AGREGAR EL PUNTO DE EMISIÓN
        factura.setFkPtoEmision(ptoOptional.get());
        // AGREGAR EL CLIENTE
        factura.setFkCliente(clienteOptional.get());
        // CREAR LA CLAVE DE ACCESO
        factura.setClaveAcceso(generaClave(
                new Fecha().getDateDDMMYYYY(),
                TipoComprobante.FACTURA.getCodigo(),
                ruc,
                AmbienteSri.TEST.getUrl(),
                ptoOptional.get().getFkEstablecimiento().getCodigo() + ptoOptional.get().getCodigo(),
                generarSecuencial(Integer.parseInt(ptoOptional.get().getSecuenciaFactura())),
                "12345678",
                TipoEmision.NORMAL.getCodigo()));
        // AGREGAR EL ESTADO DE LA FACTURA
        factura.setStatus(ComprobanteStatus.CREADO);
        // AGREGAR EL AMBIENTE
        factura.setAmbiente(ptoOptional.get().getFkEstablecimiento().getFkEmisor().getAmbiente());
        // AGREGAR EL TIPO DE EMISIÓN
        factura.setTipoEmision(ptoOptional.get().getFkEstablecimiento().getFkEmisor().getTipoEmision());
        // AGREGAR EL SECUENCIAL DE LA FACTURA
        factura.setSecuencial(generarSecuencial(Integer.parseInt(ptoOptional.get().getSecuenciaFactura())));

        facturaRepository.save(factura);
        Optional<Factura> facturaOptional = facturaRepository.findByClaveAcceso(factura.getClaveAcceso());

        /*
         * GESTIONAR EL DETALLE DE LA FACTURA Y VALIDAR SU RESPECTIVO STOCK
         */
        for (DetalleFactura detalle : detalleFacturas) {
            Optional<Producto> productoOptional = productoRepository.findById(detalle.getFkProducto().getIdProducto());

            if (!productoOptional.isPresent()) {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("El producto en el detalle no existe")
                        .build();
            }
            Producto producto = productoOptional.get();

            // Verificar si hay suficiente stock
            if (producto.getStock() < detalle.getCantidad()) {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message("Stock insuficiente para el producto: " + producto.getNombre())
                        .build();
            }
            // Actualizar el stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // Calcular impuestos
            double iva = 0.0;
            double ice = 0.0;
            double irbpn = 0.0;

            if (producto.getFkIce() != null && producto.getFkIva() == null && producto.getFkIrbpnr() == null) {
                ice = producto.getFkIce().getTarifa() / 100 * (producto.getPrecioUnitario() * detalle.getCantidad());
            }
            if (producto.getFkIrbpnr() != null && producto.getFkIce() == null && producto.getFkIva() == null) {
                irbpn = producto.getFkIrbpnr().getTarifa() / 100
                        * (producto.getPrecioUnitario() * detalle.getCantidad());
            }
            if (producto.getFkIva() != null && producto.getFkIrbpnr() == null && producto.getFkIce() == null) {
                iva = producto.getFkIva().getTarifa() / 100 * (producto.getPrecioUnitario() * detalle.getCantidad());
            }

            // Asignar valores calculados al detalle de factura
            // Agregar los impuestos al total de la factura
            if (factura.getIva() == null) {
                factura.setIva(iva);
            } else {
                factura.setIva(factura.getIva() + iva);
            }

            if (factura.getValorIce() == null) {
                factura.setValorIce(ice);
            } else {
                factura.setValorIce(factura.getValorIce() + ice);
            }

            if (factura.getValorIrbpn() == null) {
                factura.setValorIrbpn(irbpn);
            } else {
                factura.setValorIrbpn(factura.getValorIrbpn() + irbpn);
            }

            // Guardar el detalle de la factura
            detalle.setCantidad(detalle.getCantidad());
            detalle.setNombre(producto.getNombre());
            detalle.setIva(iva);
            detalle.setCodigo(producto.getCodPrincipal());
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.setSubTotal(detalle.getCantidad() * producto.getPrecioUnitario());
            detalle.setFkFactura(facturaOptional.get());
            detalleFacturaRepository.save(detalle);
            detalleFacturaRepository.save(detalle);
        }

        // Calcular otros campos de la factura referentes a impuestos
        double totalSinImpuestos = 0.0;
        double subTotal0 = 0.0;
        double subTotalNoIva = 0.0;
        double subTotalExcentoIva = 0.0;
        double totaldescuento = 0.0;

        for (DetalleFactura detalle : detalleFacturas) {
            totalSinImpuestos += detalle.getSubTotal();
            if (detalle.getFkProducto().getFkIva() != null) {
                switch (detalle.getFkProducto().getFkIva().getCodigoPorcentaje()) {
                    case "0":
                        subTotal0 += detalle.getSubTotal();
                        break;
                    case "2":
                        subTotalNoIva += detalle.getSubTotal();
                        break;
                    case "6":
                        subTotalExcentoIva += detalle.getSubTotal();
                        break;
                }
            }
            totaldescuento += detalle.getDescuento() != null ? detalle.getDescuento() : 0.0;
        }

        factura.setTotalSinImpuestos(totalSinImpuestos);
        factura.setSubTotal0(subTotal0);
        factura.setSubTotalNoIva(subTotalNoIva);
        factura.setSubTotalExcentoIva(subTotalExcentoIva);
        factura.setTotaldescuento(totaldescuento);

        // Calcular el subtotal
        double subTotal = totalSinImpuestos + subTotal0 + subTotalNoIva + subTotalExcentoIva;
        factura.setSubTotal(subTotal);

        // Calcular el valor total
        double valorTotal = subTotal + factura.getIva() + factura.getValorIce() + factura.getValorIrbpn()
                - factura.getTotaldescuento();
        factura.setValorTotal(valorTotal);

        factura.setIdFactura(facturaOptional.get().getIdFactura());
        facturaRepository.save(factura);

        // Actualizar el valor del secuencial de la factura
        PtoEmision ptoEmision = ptoOptional.get();
        ptoEmision.setSecuenciaFactura(generarSecuencial(Integer.parseInt(ptoOptional.get().getSecuenciaFactura())));
        ptoEmisionRepository.save(ptoEmision);

        Respuesta response = createXML(ruc, facturaOptional.get().getFkCliente().getIdentificacion(),
                facturaOptional.get().getClaveAcceso());

        if (response.getType() == RespuestaType.SUCCESS) {
            Respuesta firmaResponse = firmarXML(ruc, clienteOptional.get().getIdentificacion(),
                    facturaOptional.get().getClaveAcceso());

            if (firmaResponse.getType() == RespuestaType.SUCCESS) {

                return Respuesta.builder()
                        .type(RespuestaType.SUCCESS)
                        .message("Factura guardada correctamente")
                        .build();
            } else {
                return Respuesta.builder()
                        .type(RespuestaType.WARNING)
                        .message(firmaResponse.getMessage())
                        .build();
            }

        } else {
            return Respuesta.builder()
                    .type(RespuestaType.WARNING)
                    .message("Factura no se guardo correctamente")
                    .build();

        }
    }

    @Override
    public Respuesta update(Factura factura) {
        return null;
    }

    @Override
    public Respuesta enviarSRI(String claveAcceso) {
        return null;
    }

    @Override
    public Respuesta anularSRI(String claveAcceso) {
        return null;
    }

    @Override
    public Respuesta deleteFactura(String ruc, String claveAcceso) {
        return null;
    }

    @Override
    public Respuesta createXML(String ruc, String cliente, String claveAcceso) {
        // VALIDAR SI LOS DATOS DE LA FACTURA CON LA BASE DE DATOS PARA GENERAR EL XML
        Optional<Factura> factura = facturaRepository.findByClaveAcceso(claveAcceso);
        if (!factura.isPresent()) {
            return Respuesta.builder()
                    .message("No existe la factura registrada")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        // VALIDAR SI EL RUC CORRESPONDE A LA FACTURA
        if (!factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc().equals(ruc)) {
            return Respuesta.builder()
                    .message("El ruc no corresponde a la factura")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        // VALIDAR LOS DATOS DEL CLIENTE CON LA FACTURA
        if (!factura.get().getFkCliente().getIdentificacion().equals(cliente)) {
            return Respuesta.builder()
                    .message("El cliente no corresponde a la factura")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        List<DetalleFactura> listaDetalle = detalleFacturaRepository.findByFkFactura(factura.get());

        try {

            StringBuilder build = new StringBuilder();
            // Obtener la fecha de emisión como un objeto ZonedDateTime
            ZonedDateTime fechaEmisionZoned = factura.get().getFechaemision();

            // Convertir la fecha de ZonedDateTime a Date
            Date fechaEmisionDate = Date.from(fechaEmisionZoned.toInstant());

            // Formatear la fecha de emisión
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fechaEmisionFormateada = formato.format(fechaEmisionDate);

            build.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            build.append("<factura id=\"comprobante\" version=\"1.1.0\">\n");

            // Información tributaria
            build.append("<infoTributaria>\n");
            build.append("    <ambiente>").append(factura.get().getAmbiente()).append("</ambiente>\n");
            build.append("    <tipoEmision>1</tipoEmision>\n");
            build.append("    <razonSocial>")
                    .append(
                            factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRazonSocial())
                    .append("</razonSocial>\n");
            build.append("    <nombreComercial>")
                    .append(
                            factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getNombreComercial())
                    .append("</nombreComercial>\n");
            build.append("    <ruc>")
                    .append(
                            factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc())
                    .append("</ruc>\n");
            build.append("    <claveAcceso>").append(claveAcceso).append("</claveAcceso>\n");
            build.append("    <codDoc>01</codDoc>\n");
            build.append("    <estab>").append(factura.get().getFkPtoEmision().getFkEstablecimiento().getCodigo())
                    .append("</estab>\n");
            build.append("    <ptoEmi>").append(factura.get().getFkPtoEmision().getCodigo()).append("</ptoEmi>\n");
            build.append("    <secuencial>").append(factura.get().getSecuencial()).append("</secuencial>\n");
            build.append("    <dirMatriz>")
                    .append(
                            factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getDireccionMatriz())
                    .append("</dirMatriz>\n");
            build.append("</infoTributaria>\n");

            // Información de la factura
            build.append("<infoFactura>\n");
            build.append("    <fechaEmision>").append(fechaEmisionFormateada)
                    .append("</fechaEmision>\n");
            build.append("    <dirEstablecimiento>")
                    .append(factura.get().getFkPtoEmision().getFkEstablecimiento().getDireccion())
                    .append("</dirEstablecimiento>\n");
            build.append("    <contribuyenteEspecial>0047</contribuyenteEspecial>\n");
            build.append("    <obligadoContabilidad>").append(
                    factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getObligadoContabilidad())
                    .append("</obligadoContabilidad>\n");
            build.append("    <tipoIdentificacionComprador>")
                    .append(factura.get().getFkCliente().getTipoIdentificacion())
                    .append("</tipoIdentificacionComprador>\n");
            build.append("    <razonSocialComprador>")
                    .append(factura.get().getFkCliente().getNombre())
                    .append("</razonSocialComprador>\n");
            build.append("    <identificacionComprador>")
                    .append(factura.get().getFkCliente().getIdentificacion())
                    .append("</identificacionComprador>\n");
            build.append("    <totalSinImpuestos>").append(factura.get().getSubTotal())
                    .append("</totalSinImpuestos>\n");
            build.append("    <totalDescuento>").append(factura.get().getTotaldescuento())
                    .append("</totalDescuento>\n");
            build.append("    <totalConImpuestos>\n");
            build.append("        <totalImpuesto>\n");
            /*
             * Aqui viene la configuracion del impuesto actual
             */
            build.append("            <codigo>4</codigo>\n");

            build.append("            <codigoPorcentaje>2</codigoPorcentaje>\n");
            build.append("            <baseImponible>").append(factura.get().getTotalSinImpuestos())
                    .append("</baseImponible>\n");
            build.append("            <tarifa>12.00</tarifa>\n");
            build.append("            <valor>").append(factura.get().getIva()).append("</valor>\n");
            build.append("        </totalImpuesto>\n");
            build.append("    </totalConImpuestos>\n");
            build.append("    <propina>0</propina>\n");
            build.append("    <importeTotal>").append(factura.get().getValorTotal())
                    .append("</importeTotal>\n");
            build.append("    <moneda>USD</moneda>\n");
            build.append("    <pagos>\n");
            build.append("        <pago>\n");
            build.append("            <formaPago>" + factura.get().getFormaPago() + "</formaPago>\n");
            build.append("            <total>").append(factura.get().getValorTotal()).append("</total>\n");
            build.append("            <plazo>")
                    .append(factura.get().getPlazo() == null ? "0" : factura.get().getPlazo()).append("</plazo>\n");
            build.append("            <unidadTiempo>").append("dias").append("</unidadTiempo>\n");
            build.append("        </pago>\n");
            build.append("    </pagos>\n");
            build.append("    <valorRetIva>0.00</valorRetIva>\n");
            build.append("    <valorRetRenta>0.00</valorRetRenta>\n");
            build.append("</infoFactura>\n");

            // Detalles de la factura
            build.append("<detalles>\n");
            for (DetalleFactura detalle : listaDetalle) {
                Optional<Producto> productoOptional = productoRepository
                        .findById(detalle.getFkProducto().getIdProducto());
                Producto producto = productoOptional.get();
                build.append("    <detalle>\n");
                build.append("        <codigoPrincipal>").append(producto.getCodPrincipal())
                        .append("</codigoPrincipal>\n");
                build.append("        <descripcion>").append(producto.getNombre()).append("</descripcion>\n");
                build.append("        <cantidad>").append(detalle.getCantidad()).append("</cantidad>\n");
                build.append("        <precioUnitario>").append(detalle.getPrecioUnitario())
                        .append("</precioUnitario>\n");
                build.append("        <descuento>")
                        .append(detalle.getDescuento() == null ? "0.00" : detalle.getDescuento())
                        .append("</descuento>\n");
                build.append("        <precioTotalSinImpuesto>").append(detalle.getPrecioUnitario())
                        .append("</precioTotalSinImpuesto>\n");
                build.append("        <impuestos>\n");

                // Impuesto IVA
                if (detalle.getIva() != null && producto.getFkIva() != null) {
                    build.append("            <impuesto>\n");
                    build.append("                <codigo>").append(producto.getFkIva().getCodigoPorcentaje())
                            .append("</codigo>\n");
                    build.append("                <codigoPorcentaje>").append(producto.getFkIva().getCodigoPorcentaje())
                            .append("</codigoPorcentaje>\n");
                    build.append("                <tarifa>").append(producto.getFkIva().getTarifa())
                            .append("</tarifa>\n");
                    double baseImponibleIVA = detalle.getCantidad() * detalle.getPrecioUnitario();
                    double valorImpuestoIVA = baseImponibleIVA * (producto.getFkIva().getTarifa() / 100);
                    build.append("                <baseImponible>").append(baseImponibleIVA)
                            .append("</baseImponible>\n");
                    build.append("                <valor>").append(valorImpuestoIVA).append("</valor>\n");
                    build.append("            </impuesto>\n");
                }

                // Impuesto ICE
                if (detalle.getIce() != null && producto.getFkIce() != null) {
                    build.append("            <impuesto>\n");
                    build.append("                <codigo>").append(producto.getFkIce().getCodigoPorcentaje())
                            .append("</codigo>\n");
                    // Agrega aquí los campos correspondientes al ICE
                    build.append("            </impuesto>\n");
                }

                // Impuesto IRBPNR
                if (producto.getFkIrbpnr() != null) {
                    build.append("            <impuesto>\n");
                    build.append("                <codigo>").append(producto.getFkIrbpnr().getCodigoPorcentaje())
                            .append("</codigo>\n");
                    // Agrega aquí los campos correspondientes al IRBPNR
                    build.append("            </impuesto>\n");
                }

                build.append("        </impuestos>\n");
                build.append("    </detalle>\n");
            }
            build.append("</detalles>\n");

            // Información adicional
            build.append("<infoAdicional>\n");
            build.append("    <campoAdicional nombre=\"DIRECCION\">")
                    .append(factura.get().getFkCliente().getDireccion())
                    .append("</campoAdicional>\n");
            build.append("    <campoAdicional nombre=\"E-MAIL\">").append(factura.get().getFkCliente().getEmail())
                    .append("</campoAdicional>\n");
            build.append("    <campoAdicional nombre=\"TELEFONO\">").append(factura.get().getFkCliente().getTelefono())
                    .append("</campoAdicional>\n");
            build.append("    <campoAdicional nombre=\"PLAZO\">").append("</campoAdicional>\n");
            build.append("    <campoAdicional nombre=\"DIAS\">")
                    .append(factura.get().getPlazo() == null ? "0" : factura.get().getPlazo())
                    .append("</campoAdicional>\n");
            build.append("</infoAdicional>\n");
            build.append("</factura>");

            // Nombre del archivo XML
            String nombreArchivo = "FACT_" + claveAcceso + ".xml";

            // Guardar el XML en un archivo dentro de la carpeta
            File carpeta = new File(uploadDirectory + "/"
                    + factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc() + "/Clientes/"
                    + factura.get().getFkCliente().getIdentificacion()
                    + "/Facturas/" + factura.get().getClaveAcceso());
            if (!carpeta.exists()) {
                carpeta.mkdirs(); // Crear la carpeta si no existe
            }

            File archivo = new File(carpeta, nombreArchivo);
            FileWriter writer = new FileWriter(archivo);
            writer.write(build.toString());
            writer.close();

        } catch (Exception ex) {
            System.out.println("ERROR EN LA GENERACION DE XML FACTURA: " + ex.getMessage());
            ex.printStackTrace();
        }

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .message("XML DE FACTURA REALIZADA CON EXITO")
                .build();
    }

    @Override
    public Respuesta firmarXML(String ruc, String cliente, String claveAcceso) {
        // VALIDAR SI LOS DATOS DE LA FACTURA CON LA BASE DE DATOS PARA GENERAR EL XML
        Optional<Factura> factura = facturaRepository.findByClaveAcceso(claveAcceso);
        if (!factura.isPresent()) {
            return Respuesta.builder()
                    .message("No existe la factura registrada")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        // VALIDAR SI EL RUC CORRESPONDE A LA FACTURA
        if (!factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc().equals(ruc)) {
            return Respuesta.builder()
                    .message("El ruc no corresponde a la factura")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        // VALIDAR LOS DATOS DEL CLIENTE CON LA FACTURA
        if (!factura.get().getFkCliente().getIdentificacion().equals(cliente)) {
            return Respuesta.builder()
                    .message("El cliente no corresponde a la factura")
                    .type(RespuestaType.WARNING)
                    .build();
        }
        try {
            XAdESBESSignature.firmar(
                    uploadDirectory + "/"
                            + factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc()
                            + "/Clientes/"
                            + factura.get().getFkCliente().getIdentificacion()
                            + "/Facturas/" + factura.get().getClaveAcceso() + "/" + "FACT_" + claveAcceso + ".xml",
                    uploadDirectory + "/"
                            + factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc()
                            + "/configuration/"
                            + factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getDirFirma(),
                    factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getPassFirma(),
                    uploadDirectory + "/"
                            + factura.get().getFkPtoEmision().getFkEstablecimiento().getFkEmisor().getRuc()
                            + "/Clientes/"
                            + factura.get().getFkCliente().getIdentificacion()
                            + "/Facturas/" + factura.get().getClaveAcceso(),
                    "FACT_" + claveAcceso + ".xml");

        } catch (Exception e) {
            return Respuesta.builder()
                    .message(e.toString())
                    .type(RespuestaType.WARNING)
                    .build();
        }
        return Respuesta.builder()
                .message("Factura firmada correctamente")
                .type(RespuestaType.SUCCESS)
                .build();

    }

    @Override
    public Page<Factura> getByRuc(String ruc, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Factura> getAll(Pageable pageable) {
        return null;
    }

    /**
     * GENERADOR DE LA CLAVE DE ACCESO DEL SRI.
     *
     * @param fechaEmision      Fecha de emisión del comprobante (en formato
     *                          ddMMyyyy).
     * @param tipoComprobante   Tipo de comprobante (por ejemplo: "01" para
     *                          factura).
     * @param ruc               RUC del emisor del comprobante.
     * @param ambiente          Ambiente de emisión del comprobante (por ejemplo:
     *                          "1" para producción).
     * @param serie             Serie del comprobante.
     * @param numeroComprobante Número del comprobante.
     * @param codigoNumerico    Código numérico del comprobante.
     * @param tipoEmision       Tipo de emisión del comprobante (por ejemplo: "1"
     *                          para normal).
     * @return La clave de acceso generada.
     */
    public String generaClave(String fechaEmision,
            String tipoComprobante,
            String ruc,
            String ambiente,
            String serie,
            String numeroComprobante,
            String codigoNumerico,
            String tipoEmision) {
        String claveGenerada = "";
        int verificador = 0;

        // Asegurarse de que el RUC tenga 13 caracteres
        if ((ruc != null) && (ruc.length() < 13)) {
            ruc = String.format("%013d", new Object[] { ruc });
        }

        // Construir la clave de acceso
        StringBuilder clave = new StringBuilder(fechaEmision);
        clave.append(tipoComprobante);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(serie);
        clave.append(numeroComprobante);
        clave.append(codigoNumerico);
        clave.append(tipoEmision);

        // Generar el dígito verificador utilizando el algoritmo módulo 11
        verificador = generaDigitoModulo11(clave.toString());
        clave.append(Integer.valueOf(verificador));
        claveGenerada = clave.toString();

        // Verificar que la clave generada tenga la longitud esperada
        if (clave.toString().length() != 49) {
            claveGenerada = null;
        }
        return claveGenerada;
    }

    /**
     * Método para generar el dígito verificador utilizando el algoritmo módulo 11.
     *
     * @param cadena Cadena de dígitos sobre la cual se calculará el dígito
     *               verificador.
     * @return El dígito verificador calculado.
     */
    public int generaDigitoModulo11(String cadena) {
        int baseMultiplicador = 7;
        int[] aux = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;

        // Convertir la cadena en un arreglo de enteros y calcular el total ponderado
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt("" + cadena.charAt(i));
            aux[i] *= multiplicador;
            multiplicador++;
            if (multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }

        // Calcular el dígito verificador
        if ((total == 0) || (total == 1)) {
            verificador = 0;
        } else {
            verificador = 11 - total % 11 == 11 ? 0 : 11 - total % 11;
        }

        // Ajustar el dígito verificador si es 10
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }

    /**
     * GENEAR EL SECUENCIAL DE UNA FACTURA Y TAMBIEN SUMARLE 1
     */
    public static String generarSecuencial(int numero) {
        // Sumar 1 al número recibido como parámetro
        int nuevoNumero = numero + 1;

        // Generar el secuencial de 9 dígitos rellenando con ceros a la izquierda
        String secuencial = String.format("%09d", nuevoNumero);

        return secuencial;
    }

}
