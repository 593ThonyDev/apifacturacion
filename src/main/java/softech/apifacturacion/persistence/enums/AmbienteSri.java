package softech.apifacturacion.persistence.enums;

public enum AmbienteSri {

    TEST("1"),
    PRODUCCION("2"),
    TEST_RECEPCION("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl"),
    TEST_AUTORIZACION("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl"),
    RECEPCION_URL("https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl"),
    AUTORIZACION_URL("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");

    private final String url;

    private AmbienteSri(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
