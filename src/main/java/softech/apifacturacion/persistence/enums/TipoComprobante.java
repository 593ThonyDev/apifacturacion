package softech.apifacturacion.persistence.enums;

/*
 * En este enum retorna los datos correspondientes a los codigos al momento de enviar los datos al sri
 */
public enum TipoComprobante {

    FACTURA("01"),
    LIQUIDACION("03"),
    NOTA_CREDITO("04"),
    NOTA_DEBITO("05"),
    GUIA_REMISION("06"),
    COMPROBANTE_RETENCION("07");

    private final String codigo;

    private TipoComprobante(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
