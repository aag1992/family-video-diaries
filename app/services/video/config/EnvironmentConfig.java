package services.video.config;

import com.google.inject.Inject;
import com.typesafe.config.Config;

public class EnvironmentConfig {


    private final String downloadTarget;

    @Inject
    public EnvironmentConfig(Config configuration) {
        this.downloadTarget = configuration.getString("env.download_target");
    }

    public String getDownloadTargetName() {
        return downloadTarget;
    }
}
