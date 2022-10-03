package controllers;

import com.google.inject.Inject;
import model.video.join.VideoJoiningDetails;
import model.video.segments.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.video.joining.VideoJoiningService;
import services.video.segmenting.VideoSegmentingService;
import services.video.upload.VideoUploadService;
import services.cloud.gcloud.GCloudAdapter;
import services.video.config.EnvironmentConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static services.video.consts.VideoManipulationConsts.*;

public class VideoController extends Controller {

    private final GCloudAdapter gCloudAdapter;
    private final VideoUploadService videoUploadManager;
    private final VideoSegmentingService segmentingManager;
    private final VideoJoiningService videoJoiningService;

    private final EnvironmentConfig environmentConfig;

    @Inject
    public VideoController(GCloudAdapter gCloudAdapter,
                           VideoUploadService videoUploadManager,
                           EnvironmentConfig environmentConfig,
                           VideoSegmentingService segmentingManager,
                           VideoJoiningService videoJoiningService) {
        this.gCloudAdapter = gCloudAdapter;
        this.videoUploadManager = videoUploadManager;
        this.environmentConfig = environmentConfig;
        this.segmentingManager = segmentingManager;
        this.videoJoiningService = videoJoiningService;
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> uploadVideo(Http.Request request) {
        try {
            VideoUploadDetails uploadDetails = videoUploadManager.getUploadDetailsFromRequest(request, UPLOAD_VIDEO_FILE_KEY);
            gCloudAdapter.upload(uploadDetails);
            return CompletableFuture.completedFuture(ok("Successfully uploaded file"));
        } catch (Exception e) {
            Logger.of(VideoController.class).error("Error " + e.getClass() + " while trying to upload video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to upload video " + e.getMessage()));
        }
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> segmentVideo(Http.Request request) {
        try {
            VideoSegmentingDetails segmentingDetails = segmentingManager.getVideoSegmentingDetailsFromRequest(request, SEGMENTS_FILE_KEY);
            gCloudAdapter.download(segmentingDetails, environmentConfig.getDownloadTarget());
            segmentingManager.segmentVideo(segmentingDetails.getFile(), this.environmentConfig.getDownloadTarget(), this.environmentConfig.getSegmentTargetDirTarget());
            return CompletableFuture.completedFuture(ok("Successfully segmented video file"));
        } catch (Exception e) {
            Logger.of(VideoController.class).error("Error " + e.getClass() + " while trying to sgement video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to segment video " + e.getMessage()));
        }
    }


    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> join(Http.Request request) {
        try {
            VideoJoiningDetails joiningDetails = videoJoiningService.getJoiningDetailsFromRequest(request, JOIN_FILE_KEY);
            videoJoiningService.joinVideos(joiningDetails.getFile(), environmentConfig.getJoiningTarget());
            return CompletableFuture.completedFuture(ok("Successfully joined video"));
        } catch (Exception e) {
            Logger.of(VideoController.class).error("Error " + e.getClass() + " while trying to join videos " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to join videos " + e.getMessage()));
        }
    }
}
