package softech.apifacturacion.upload.implementation;

import java.nio.file.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import softech.apifacturacion.upload.Directory;

@Service
public class DirectoryImpl implements Directory {

    private static final Logger logger = LoggerFactory.getLogger(UploadImageImpl.class);

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public String createDirectory(String folder) {
        try {
            Path directory = Paths.get(uploadDirectory + "/" + folder);
            if (!Files.isDirectory(directory)) {
                Files.createDirectories(directory);
            }
            return directory.toAbsolutePath().toString();
        } catch (Exception e) {
            logger.error("No se pudo crear el directorio");
            return null;
        }
    }

    @Override
    public Boolean deleteDirectory(String folder) {
        try {
            Path directory = Paths.get(uploadDirectory + "/" + folder);
            if (Files.isDirectory(directory)) {
                Files.delete(directory);
            }
            return true;
        } catch (Exception e) {
            logger.error("No se pudo eliminar el directorio");
            return false;
        }
    }

}
