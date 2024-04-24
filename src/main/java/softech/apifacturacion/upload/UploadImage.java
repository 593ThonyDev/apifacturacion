package softech.apifacturacion.upload;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImage {
    
    /**
     * Agrega una imagen al directorio especificado.
     * 
     * @param folder    la carpeta donde se almacenará la imagen.
     * @param imageName el nombre de archivo de la imagen, incluida la extensión.
     * @param file      el archivo de imagen a subir.
     * @return el nombre de archivo con la extensión si el archivo se subió
     *         correctamente, de lo contrario, devuelve null
     */
    String addImage(String folder, String imageName, MultipartFile file);

     /**
     * Elimina una imagen del directorio especificado.
     * 
     * @param folder   la carpeta donde se encuentra la imagen a eliminar.
     * @param fileName el nombre de archivo de la imagen a eliminar, incluida la
     *                 extensión.
     * @return un mensaje indicando si la imagen se eliminó correctamente o si
     *         ocurrió un error.
     */
    Boolean deleteImage(String folder, String imageName);
}
