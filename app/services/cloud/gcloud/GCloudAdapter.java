package services.cloud.gcloud;


import com.google.cloud.storage.*;
import com.google.inject.Inject;
import exceptions.VideoNotUploadedException;
import model.video.upload.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.Logger;
import services.cloud.gcloud.config.GCloudConfig;
import services.cloud.iCloudService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GCloudAdapter implements iCloudService {


    private final GCloudConfig config;

    @Inject
    public GCloudAdapter(GCloudConfig config) {
        this.config = config;
    }

    public void upload(VideoUploadDetails file) throws IOException {
        Logger.of(GCloudAdapter.class).info("Starting file upload");
        Storage storage = StorageOptions.newBuilder().setProjectId(config.getProjectId()).build().getService();
        BlobId blobId = BlobId.of(config.getBucket(), file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(file.getFile().getPath())));
        Logger.of(GCloudAdapter.class).debug("finished file upload");

    }

    public void download(VideoSegmentingDetails segmentingDetails, String targetPath) throws VideoNotUploadedException {
        Storage storage = StorageOptions.newBuilder().setProjectId(config.getProjectId()).build().getService();
        Blob blob = storage.get(BlobId.of(config.getBucket(), segmentingDetails.getVideoPath()));
        if (blob.exists()) {
            blob.downloadTo(Paths.get(targetPath));
        } else {
            throw new VideoNotUploadedException("Failed to find video in cloud");
        }
    }
}
