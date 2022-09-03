package services.cloud;

import model.file.upload.FileUploadDetails;

import java.io.IOException;

public interface iCloudService {
    void upload(FileUploadDetails file) throws IOException;
}
