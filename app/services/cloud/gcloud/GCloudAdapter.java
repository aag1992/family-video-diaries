package services.cloud.gcloud;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.inject.Inject;
import model.file.upload.FileUploadDetails;
import play.Logger;
import services.cloud.gcloud.config.GCloudConfig;
import services.cloud.iCloudService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GCloudAdapter implements iCloudService {


    private final GCloudConfig config;

    @Inject
    public GCloudAdapter(GCloudConfig config)  {
        this.config = config;
    }

    public void upload(FileUploadDetails file) throws IOException {
        Logger.of(GCloudAdapter.class).info("Starting file upload");
        Storage storage = StorageOptions.newBuilder().setProjectId(config.getProjectId()).build().getService();
        BlobId blobId = BlobId.of(config.getBucket(), file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(file.getFile().getPath())));
        Logger.of(GCloudAdapter.class).debug("finished file upload");
    }


}
