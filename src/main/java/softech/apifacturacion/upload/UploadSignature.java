package softech.apifacturacion.upload;

import org.springframework.web.multipart.MultipartFile;

public interface UploadSignature {

    /**
     * Agrega una imagen al directorio especificado.
     * 
     * @param folder    la carpeta donde se almacenará la imagen.
     * @param imageName el nombre de archivo de la imagen, incluida la extensión.
     * @param file      el archivo de firma a subir.
     * @return el nombre de archivo con la extensión si el archivo se subió
     *         correctamente, de lo contrario, devuelve null
     */
    String addSignature(String folder, String signatureName, MultipartFile file);

     /**
     * Elimina una firma del directorio especificado.
     * 
     * @param folder   la carpeta donde se encuentra la firma a eliminar.
     * @param fileName el nombre de archivo de la firma a eliminar, incluida la
     *                 extensión.
     * @return un mensaje indicando si la firma se eliminó correctamente o si
     *         ocurrió un error.
     */
    Boolean deleteSignature(String folder, String imageName);

}
