package services.video.config;

import com.google.inject.Inject;
import com.typesafe.config.Config;

public class EnvironmentConfig {


    private final String downloadTarget;
    private final String segmentTargetDir;


    @Inject
    public EnvironmentConfig(Config configuration) {
        this.downloadTarget = configuration.getString("env.download_target");
        this.segmentTargetDir = configuration.getString("env.segments_dir");
    }

    public String getDownloadTarget() {
        return downloadTarget;
    }

    public String getSegmentTargetDirTarget() {
        return segmentTargetDir;
    }

}
