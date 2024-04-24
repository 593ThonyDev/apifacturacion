package softech.apifacturacion.upload.implementation;

import java.io.IOException;
import java.nio.file.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import softech.apifacturacion.upload.UploadSignature;

@Service
public class UploadSignatureImpl implements UploadSignature {

    private static final Logger logger = LoggerFactory.getLogger(UploadSignatureImpl.class);

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public String addSignature(String folder, String signatureName, MultipartFile file) {
        try {
            // Obtenemos el nombre del archivo
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null && isSignature(originalFileName)) {
                Path directory = Paths.get(uploadDirectory + "/" + folder);
                if (!Files.isDirectory(directory)) {
                    Files.createDirectories(directory);
                }

                // Generamos un nombre único para el archivo con su extensión original
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = signatureName + extension;

                Path filePath = directory.resolve(fileName).normalize();
                // Verificamos si ya existe un archivo con el mismo nombre y lo eliminamos
                deleteSignatureIfExists(directory, fileName);

                Files.copy(file.getInputStream(), filePath);
                return fileName;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error al subir el logotipo: " + e.getMessage());
            return null; // Error al subir el archivo
        }
    }

    @Override
    public Boolean deleteSignature(String folder, String fileName) {
        try {

            Path filePath = Paths.get(uploadDirectory + "/" + folder + "/" + fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            logger.error("Error al eliminar la firma: " + e);
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si el archivo es una firma basándose en su extensión.
     * 
     * @param fileName el nombre de archivo con la extensión.
     * @return true si el archivo es una firma, false si no lo es.
     */
    private boolean isSignature(String fileName) {
        String[] allowedExtensions = { "p12", "pfx" };
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        for (String ext : allowedExtensions) {
            if (extension.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si ya existe un archivo con el mismo nombre en la carpeta
     * especificada y lo elimina.
     * 
     * @param directory la ruta del directorio donde se buscará el archivo.
     * @param fileName  el nombre del archivo a eliminar.
     */
    private void deleteSignatureIfExists(Path directory, String fileName) {
        try {
            Path existingFilePath = directory.resolve(fileName).normalize();
            if (Files.exists(existingFilePath)) {
                Files.delete(existingFilePath);
            }
        } catch (IOException e) {
            logger.error("Error al eliminar el archivo existente: " + e.getMessage());
        }
    }

}
