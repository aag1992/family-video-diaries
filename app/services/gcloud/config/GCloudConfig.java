package services.gcloud.config;

import com.google.inject.Inject;
import com.typesafe.config.Config;

public class GCloudConfig {

    private final String projectId;
    private final String bucket;

    @Inject
    public GCloudConfig(Config configuration) {
        this.projectId = configuration.getString("g3.projectId");
        this.bucket = configuration.getString("g3.bucket");
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBucket() {
        return bucket;
    }
}
