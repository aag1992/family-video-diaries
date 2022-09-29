package model.video.upload;

import org.javatuples.KeyValue;

import java.io.File;

public class VideoSegmentingDetails {
    String videoPath;
    KeyValue<String, File> segmentsFileDetails;

    public VideoSegmentingDetails(String videoPath,String fileName, File file) {
        this.segmentsFileDetails = new KeyValue<>(fileName, file);
        this.videoPath = videoPath;
    }

    public File getFile() {
        return this.segmentsFileDetails.getValue();
    }

    public String getVideoPath() {
        return this.videoPath;
    }



}
