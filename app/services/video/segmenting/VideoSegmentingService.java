package services.video.segmenting;

import com.google.inject.Inject;
import com.opencsv.bean.CsvToBeanBuilder;
import exceptions.FileNotFoundException;
import exceptions.MissingFieldsInRequestException;
import model.video.segments.SegmentsRow;
import net.bramp.ffmpeg.FFmpegExecutor;
import model.video.segments.VideoSegmentingDetails;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;
import play.libs.Files;
import play.mvc.Http;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static services.file.FileManager.getFileFromTempFile;
import static services.video.consts.VideoManipulationConsts.PATH;

public class VideoSegmentingService {

    private final FFmpegExecutor ffmpegExecutor;

    @Inject
    public VideoSegmentingService(FFmpegExecutor fFmpegExecutor) {
        this.ffmpegExecutor = fFmpegExecutor;
    }

    public VideoSegmentingDetails getVideoSegmentingDetailsFromRequest(Http.Request request, String fileKey) throws FileNotFoundException, MissingFieldsInRequestException {
        Http.MultipartFormData<Files.TemporaryFile> multipartFormData = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> temporaryFile = multipartFormData.getFile(fileKey);
        if (temporaryFile == null) {
            throw new FileNotFoundException("Expected file in request");
        }
        if (multipartFormData.asFormUrlEncoded().get(PATH).length == 0) {
            throw new MissingFieldsInRequestException("Missing a necessary field in request");
        }

        return new VideoSegmentingDetails(multipartFormData.asFormUrlEncoded().get(PATH)[0],
                temporaryFile.getFilename(),
                getFileFromTempFile(temporaryFile));
    }


    public void segmentVideo(File segmentsFile, String fullVideoPath, String segmentTargetDirTarget) throws java.io.FileNotFoundException {
        Logger.of("Starting to segment video");
        List<SegmentsRow> segmentRows = getSegmentsFromSegmentsFile(segmentsFile);

        for (SegmentsRow segmentRow : segmentRows) {
            Logger.of(VideoSegmentingDetails.class).info("Getting builder");
            FFmpegBuilder builder = getFfmpegBuilder(fullVideoPath, segmentRow, segmentTargetDirTarget);
            Logger.of(VideoSegmentingDetails.class).info("Got builder");
            final ProgressListener progressListener = progress -> Logger.of(progress.toString());
            ffmpegExecutor.createJob(builder, progressListener).run();
            Logger.of(VideoSegmentingDetails.class).info("Created ffmpeg job");
        }
    }

    private FFmpegBuilder getFfmpegBuilder(String fullVideoPath, SegmentsRow segmentRow, String segmentTargetDirTarget) {

        FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                .overrideOutputFiles(true).addInput(fullVideoPath);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss.SSS");
        DateTime start = fmt.parseDateTime(segmentRow.startTime);
        DateTime end = fmt.parseDateTime(segmentRow.endTime);
        fFmpegBuilder.addOutput(segmentTargetDirTarget + segmentRow.videoName)
                .setStartOffset(start.getMillisOfDay(), TimeUnit.MILLISECONDS)
                .setDuration(end.getMillisOfDay() - start.getMillisOfDay(), TimeUnit.MILLISECONDS)
                .setVideoCodec("copy");
        return fFmpegBuilder;
    }

    private List<SegmentsRow> getSegmentsFromSegmentsFile(File segmentsFile) throws java.io.FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(segmentsFile))
                .withType(SegmentsRow.class)
                .build()
                .parse();
    }
}
