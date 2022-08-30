package services.gcloud;


import com.google.cloud.storage.*;
import com.google.inject.Inject;
import play.Logger;
import services.gcloud.config.GCloudConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GCloudAdapter {


    private final GCloudConfig config;

    @Inject
    public GCloudAdapter(GCloudConfig config) {
        this.config = config;
    }

    public void upload(File file) throws IOException {
        Logger.of(GCloudAdapter.class).debug("Starting file upload");
        Storage storage = StorageOptions.newBuilder().setProjectId(config.getProjectId()).build().getService();
        BlobId blobId = BlobId.of(config.getBucket(), file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(file.getPath())));
        Logger.of(GCloudAdapter.class).debug("finished file upload");
    }

}
