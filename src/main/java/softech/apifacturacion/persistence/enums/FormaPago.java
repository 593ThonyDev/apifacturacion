package softech.apifacturacion.persistence.enums;

public enum FormaPago {

    SIN_SISTEMA_FINANCIERO("01"),
    COMPESACION_DEUDA("15"),
    TARJETA_DEBITO("16"),
    DINERO_ELECTRONICO("17"),
    TARJETA_PREPAGO("18"),
    TARJETA_CREDITO("19"),
    CON_SISTEMA_FINANCIERO("20"),
    ENDOSO_TITULOS("21");

    private final String codigo;

    private FormaPago(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
