package services.video.joining;

import com.google.inject.Inject;
import exceptions.FileNotFoundException;
import model.video.join.VideoJoiningDetails;
import model.video.segments.SegmentsRow;
import model.video.segments.VideoSegmentingDetails;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;
import play.libs.Files;
import play.mvc.Http;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static services.file.FileManager.getFileFromTempFile;

public class VideoJoiningService {
    private final FFmpegExecutor ffmpegExecutor;


    @Inject
    public VideoJoiningService(FFmpegExecutor fFmpegExecutor) {
        this.ffmpegExecutor = fFmpegExecutor;
    }


    public VideoJoiningDetails getJoiningDetailsFromRequest(Http.Request request, String fileKey) throws FileNotFoundException {
        Http.MultipartFormData<Files.TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        return new VideoJoiningDetails(temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile));
    }

    public void joinVideos(File file, String joiningTarget) {
        Logger.of("Starting to join frames to single video");
        FFmpegBuilder builder = getFfmpegBuilder(file.getAbsolutePath(), joiningTarget);
        Logger.of(VideoSegmentingDetails.class).info("Got builder");
        final ProgressListener progressListener = progress -> Logger.of(progress.toString());
        ffmpegExecutor.createJob(builder, progressListener).run();
        Logger.of(VideoSegmentingDetails.class).info("Created ffmpeg job");
    }

    private FFmpegBuilder getFfmpegBuilder(String filePath, String targetFile) {

        FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                .addExtraArgs("-safe","0")
                .setFormat("concat")
                .overrideOutputFiles(true)
                .addInput(filePath);
        fFmpegBuilder.addOutput(targetFile )
                .setVideoCodec("copy");
        return fFmpegBuilder;
    }

}
