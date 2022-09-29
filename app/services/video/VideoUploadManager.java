package services.video;

import exceptions.FileNotFoundException;
import exceptions.MissingFieldsInRequestException;
import model.video.upload.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.Request;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;

import static services.video.consts.VideoUploadConsts.*;

public class VideoUploadManager {
    public VideoUploadDetails getUploadDetailsFromRequest(Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        if (multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME).length == 0 || multipartFormData.asFormUrlEncoded().get(YEAR).length == 0) {
            throw new MissingFieldsInRequestException("Missing a necessary field in request");
        }

        return new VideoUploadDetails(temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile),
                multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME)[0],
                multipartFormData.asFormUrlEncoded().get(YEAR)[0]);
    }

    public VideoSegmentingDetails getVideoSegmentingDetailsFromRequest(Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        if (multipartFormData.asFormUrlEncoded().get(PATH).length == 0) {
            throw new MissingFieldsInRequestException("Missing a necessary field in request");
        }

        return new VideoSegmentingDetails(multipartFormData.asFormUrlEncoded().get(PATH)[0],
                temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile));
    }


    public VideoUploadDetails segmentVideoAndUploadSegments(Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        if (multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME).length == 0 || multipartFormData.asFormUrlEncoded().get(YEAR).length == 0) {
            throw new MissingFieldsInRequestException("Missing a necessary field in request");
        }

        return new VideoUploadDetails(temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile),
                multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME)[0],
                multipartFormData.asFormUrlEncoded().get(YEAR)[0]);
    }


    private File getFileFromTempFile(FilePart<TemporaryFile> temporaryFile) {
        TemporaryFile tempFile = temporaryFile.getRef();
        return tempFile.path().toFile();
    }

}
