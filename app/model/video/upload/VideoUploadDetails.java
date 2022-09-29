package model.video.upload;

import org.javatuples.KeyValue;

import java.io.File;

public class VideoUploadDetails {
    KeyValue<String, File> fileDetails;
    String familyMemberName;
    String year;

    public VideoUploadDetails(String fileName, File file, String familyMemberName, String year) {
        this.fileDetails = new KeyValue<>(fileName, file);
        this.familyMemberName = familyMemberName;
        this.year = year;
    }

    public File getFile() {
        return this.fileDetails.getValue();
    }

    public String getName() {
        return this.familyMemberName + "/" + this.year + "/" + this.fileDetails.getKey();
    }
}
