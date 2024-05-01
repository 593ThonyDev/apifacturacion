package softech.apifacturacion.persistence.service.implementation;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import softech.apifacturacion.persistence.enums.*;
import softech.apifacturacion.persistence.function.*;
import softech.apifacturacion.persistence.model.*;
import softech.apifacturacion.persistence.repository.*;
import softech.apifacturacion.persistence.service.*;
import softech.apifacturacion.response.*;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private final FacturaRepository facturaRepository;

    @Autowired
    private final EstablecimientoRepository establecimientoRepository;

    @Autowired
    private final EmisorRepossitory emisorRepossitory;

    @Autowired
    private final PtoEmisionRepository ptoEmisionRepository;

    @Override
    public Respuesta generateClaveAcceso(String ruc, PtoEmision ptoEmision) {
        Optional<Emisor> emisor = emisorRepossitory.findByRuc(ruc);
        if (!emisor.isPresent()) {
            return Respuesta.builder()
                    .message("El emisor no esta registrado en nuestro sistema")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        Optional<PtoEmision> optionalPto = ptoEmisionRepository.findById(ptoEmision.getIdPtoemision());
        if (!optionalPto.isPresent()) {
            return Respuesta.builder()
                    .message("El punto de emision no esta registrado en nuestro sistema")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        List<Establecimiento> establecimiento = establecimientoRepository.findByFkEmisor(emisor.get());
        if (establecimiento.isEmpty()) {
            return Respuesta.builder()
                    .message("El estableciminiento de la empresa no esta configurada")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        Factura factura = Factura.builder()
                .claveAcceso(generaClave(
                        new Fecha().getDateDDMMYYYY(),
                        TipoComprobante.FACTURA.getCodigo(),
                        ruc,
                        AmbienteSri.TEST.getUrl(),
                        optionalPto.get().getFkEstablecimiento().getCodigo() + optionalPto.get().getCodigo(),
                        generarSecuencial(Integer.parseInt(optionalPto.get().getSecuenciaFactura())),
                        "12345678",
                        TipoEmision.NORMAL.getCodigo()))
                .status(Status.CONFIGURATE)
                .build();

        Optional<Factura> optional = facturaRepository.findByClaveAcceso(factura.getClaveAcceso());
        if (optional.isPresent()) {
            return Respuesta.builder()
                    .message("Este clave de acceso ya ha sido generada")
                    .type(RespuestaType.WARNING)
                    .build();
        }

        return Respuesta.builder()
                .type(RespuestaType.SUCCESS)
                .content(new Object[] { factura })
                .build();
    }

    @Override
    public Respuesta save(Factura factura) {
        return null;
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
    public Boolean createXML(String ruc, String cliente, String claveAcceso) {
        return null;
    }

    @Override
    public Boolean firmarXML(String ruc, String cliente, String claveAcceso) {
        return null;
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
