package model.video.join;

import org.javatuples.KeyValue;

import java.io.File;

public class VideoJoiningDetails {

    KeyValue<String, File> joinFileDetails;

    public VideoJoiningDetails(String fileName, File file) {
        this.joinFileDetails = new KeyValue<>(fileName, file);
    }

    public File getFile() {
        return this.joinFileDetails.getValue();
    }

}
