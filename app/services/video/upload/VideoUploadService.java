package services.video.upload;

import exceptions.FileNotFoundException;
import exceptions.MissingFieldsInRequestException;
import model.video.upload.VideoUploadDetails;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.Request;
import play.mvc.Http.MultipartFormData.FilePart;

import static services.file.FileManager.getFileFromTempFile;
import static services.video.consts.VideoUploadConsts.*;

public class VideoUploadService {
    public VideoUploadDetails getUploadDetailsFromRequest(Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        if (!necessaryFieldsAreInForm(multipartFormData)) {
            throw new MissingFieldsInRequestException("Missing a necessary field in request");
        }

        return new VideoUploadDetails(temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile),
                multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME)[0],
                multipartFormData.asFormUrlEncoded().get(YEAR)[0]);
    }

    private boolean necessaryFieldsAreInForm(MultipartFormData<TemporaryFile> multipartFormData) {
        return (multipartFormData.asFormUrlEncoded() != null
                && multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME).length != 0
                && multipartFormData.asFormUrlEncoded().get(YEAR).length != 0);
    }

}
