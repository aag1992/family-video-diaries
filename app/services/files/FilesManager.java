package services.files;

import exceptions.FileNotFoundException;
import model.file.upload.FileUploadDetails;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.Request;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;

public class FilesManager {
    public FileUploadDetails getUploadDetailsFromRequest(Request request, String fileKey) throws FileNotFoundException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        return new FileUploadDetails(temporaryFile.getFilename(), getFileFromTempFile(temporaryFile));
    }

    private File getFileFromTempFile(FilePart<TemporaryFile> temporaryFile) {
        TemporaryFile tempFile = temporaryFile.getRef();
        return tempFile.path().toFile();
    }

}
