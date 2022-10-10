package services.video.filtering;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import controllers.VideoController;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoFilteringService {

    public List<String> filterWithConditions(Page<Blob> blobs, String year, String fileName, String segmentTargetDirTarget) throws IOException {
        List<String> files = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            if (blobMatchesConditions(blob.getName(), year, fileName)) {
                String path = segmentTargetDirTarget + blob.getName();
                File f = new File(path);
                if (downloadPrerequisites(f,blob)) {
                    blob.downloadTo(f.toPath());
                } else {
                    Logger.of(VideoController.class).info("File already exists");
                }
                files.add(blob.getName());
            }
        }
        return files;
    }

    private boolean downloadPrerequisites(File f, Blob blob) throws IOException {
        f.mkdirs();
        if(f.createNewFile()){
            return true;
        }
        else{
            return fileHasBeenUpdated(blob);
        }
    }


    private boolean blobMatchesConditions(String name, String year, String fileName) {
        return name.contains(String.valueOf(year)) && name.contains(fileName);
    }

    private boolean fileHasBeenUpdated(Blob blob) {
        return false;
    }

}
