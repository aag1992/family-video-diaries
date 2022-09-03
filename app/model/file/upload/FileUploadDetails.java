package model.file.upload;

import org.javatuples.KeyValue;

import java.io.File;

public class FileUploadDetails {
    KeyValue<String, File> details;

    public FileUploadDetails(String name, File file) {
        this.details = new KeyValue<>(name, file);
    }

    public File getFile() {
        return this.details.getValue();
    }

    public String getName() {
        return this.details.getKey();
    }

}
