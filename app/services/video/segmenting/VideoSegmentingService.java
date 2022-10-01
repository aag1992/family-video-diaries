package services.video.segmenting;

import exceptions.FileNotFoundException;
import exceptions.MissingFieldsInRequestException;
import model.video.upload.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.libs.Files;
import play.mvc.Http;

import java.io.File;

import static services.file.FileManager.getFileFromTempFile;
import static services.video.consts.VideoUploadConsts.PATH;

public class VideoSegmentingService {


    public VideoSegmentingDetails getVideoSegmentingDetailsFromRequest(Http.Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        Http.MultipartFormData<Files.TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
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


    public VideoUploadDetails segmentVideo(File segmentsFile, String fullVideo) throws FileNotFoundException, MissingFieldsInRequestException {
//        MultipartFormData<TemporaryFile> multipartFormData = request.body().asMultipartFormData();
//        FilePart<TemporaryFile> temporaryFile = multipartFormData.getFile(fullVideo);
//        if (temporaryFile == null) {
//            throw new FileNotFoundException("Expected file in request");
//        }
//        if (multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME).length == 0 || multipartFormData.asFormUrlEncoded().get(YEAR).length == 0) {
//            throw new MissingFieldsInRequestException("Missing a necessary field in request");
//        }
//
//        return new VideoUploadDetails(temporaryFile.getFilename(),
//                getFileFromTempFile(temporaryFile),
//                multipartFormData.asFormUrlEncoded().get(FAMILY_MEMBER_NAME)[0],
//                multipartFormData.asFormUrlEncoded().get(YEAR)[0]);
        return null;
    }


}
