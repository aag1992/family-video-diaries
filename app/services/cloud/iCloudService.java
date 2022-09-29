package services.cloud;

import model.video.upload.VideoUploadDetails;

import java.io.IOException;

public interface iCloudService {
    void upload(VideoUploadDetails file) throws IOException;
}
