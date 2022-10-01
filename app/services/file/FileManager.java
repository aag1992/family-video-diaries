package services.file;

import play.libs.Files;
import play.mvc.Http;

import java.io.File;

public class FileManager {


    public static File getFileFromTempFile(Http.MultipartFormData.FilePart<Files.TemporaryFile> temporaryFile) {
        Files.TemporaryFile tempFile = temporaryFile.getRef();
        return tempFile.path().toFile();
    }

}
