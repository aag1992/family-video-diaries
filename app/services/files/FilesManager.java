package services.files;

import exceptions.FileNotFoundException;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.Request;
import play.mvc.Http.MultipartFormData.FilePart;

import javax.validation.constraints.NotNull;
import java.io.File;

public class FilesManager {
    public File getUploadedFileFromRequest(Request request, String fileKey) throws FileNotFoundException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        return getFileFromTempFile(temporaryFile);
    }

    private File getFileFromTempFile(FilePart<TemporaryFile> temporaryFile) {
        TemporaryFile tempFile = temporaryFile.getRef();
        File file = tempFile.path().toFile();
        return renameFile(file, temporaryFile.getFilename());
    }

    private File renameFile(File file, @NotNull String newName) {
        return new File(String.format("%s/%s", file.getParent(), newName));
    }
}
