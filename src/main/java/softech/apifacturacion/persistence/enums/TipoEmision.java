package softech.apifacturacion.persistence.enums;

public enum TipoEmision {
    NORMAL("1");

    private final String codigo;

    private TipoEmision(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
