package softech.apifacturacion.persistence.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.persistence.enums.Status;
import softech.apifacturacion.persistence.model.Emisor;
import softech.apifacturacion.persistence.model.dto.EmisorPageDto;
import softech.apifacturacion.response.Respuesta;

public interface EmisorService {

    public Respuesta registerEmisor(Emisor emisor, String nombres, String apellidos, String password);

    /**
     * Configura los datos de la empresa.
     * 
     * @param ruc                           Ruc para validar si existe la empresa en
     *                                      el sistema
     * @param razonSocial                   Datos de la razon social segun estandar
     *                                      del SRI
     * 
     * @param nombreComercial               Datos de nombre comercial segun estandar
     *                                      del SRI
     * @param direccionMatriz               Direccion de la empresa segun estandar
     *                                      del SRI
     * @param OlibgadoALlevarContabilididad Validacion de SI o NO para la formacion
     *                                      del XML enviado al SRI
     * @param agenteResolucion              Agente de resolucion segun estandar del
     *                                      SRI puede ir nulo o con valores
     * @param regimenMicroempresa           Defirir el regimen de la microempresa
     * @param tipoContribuyente             Definir el tipo de contribuyente
     * @param passFirma                     Clave del archivo tipo pfx o p12 para
     *                                      firmar los
     *                                      comprobantes electronicos alamacenados
     *                                      en el servidor
     * @param firma                         Archivo tipo pfx o p12 para firmar los
     *                                      comprobantes electronicos alamacenados
     *                                      en el servidor,
     * @param Logotipo                      Archivo tipo imagen para los
     *                                      comprobantes electronicos gestionados
     *                                      comprobantes electronicos alamacenados
     *                                      en el servidor.
     * @return Respuesta valida o adventerncias con los errores o campos faltantes.
     */
    public Respuesta configurateEmisor(Emisor emisor, MultipartFile logo, MultipartFile firma);

    /*
     * Cambio de estatus para la gestion automatica del emisor para poder mantener
     * la logica del negocio
     * 
     * @param ruc identificador del emisor
     * 
     * @param status estado al que se desea cmabiar "ONLINE" y "OFFLINE"
     * 
     * @return Respuesta valida o adventerncias con los errores
     */
    public Respuesta changeStatus(String ruc, Status status);

    /*
     * Listar las paginas correspondentes a los emisores registrados
     */
    public Page<EmisorPageDto> getAll(Pageable pageable);

    /*
     * Cambio de estatus para la gestion automatica del emisor para poder mantener
     * la logica del negocio
     * 
     * @param ruc identificador del emisor
     * 
     * 
     * @return Respuesta valida con la informacion del Emisor, o adventerncias con
     * los errores
     */
    public Respuesta getDataByRuc(String ruc);

}
