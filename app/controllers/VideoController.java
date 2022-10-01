package controllers;

import com.google.inject.Inject;
import model.video.upload.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.video.segmenting.VideoSegmentingService;
import services.video.upload.VideoUploadService;
import services.cloud.gcloud.GCloudAdapter;
import services.video.config.EnvironmentConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static services.video.consts.VideoUploadConsts.*;

public class VideoController extends Controller {

    private final GCloudAdapter gCloudAdapter;
    private final VideoUploadService videoUploadManager;
    private final VideoSegmentingService segmentingManager;

    private final EnvironmentConfig environmentConfig;

    @Inject
    public VideoController(GCloudAdapter gCloudAdapter, VideoUploadService videoUploadManager, EnvironmentConfig environmentConfig, VideoSegmentingService segmentingManager) {
        this.gCloudAdapter = gCloudAdapter;
        this.videoUploadManager = videoUploadManager;
        this.environmentConfig = environmentConfig;
        this.segmentingManager = segmentingManager;
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> uploadVideo(Http.Request request) {
        try {
            VideoUploadDetails uploadDetails = videoUploadManager.getUploadDetailsFromRequest(request, VIDEO);
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
            VideoSegmentingDetails segmentingDetails = segmentingManager.getVideoSegmentingDetailsFromRequest(request, SEGMENTS);
            gCloudAdapter.download(segmentingDetails, environmentConfig.getDownloadTargetName());
            segmentingManager.segmentVideo(segmentingDetails.getFile(), this.environmentConfig.getDownloadTargetName());
            return CompletableFuture.completedFuture(ok("Successfully segmented video file"));
        } catch (Exception e) {
            Logger.of(VideoController.class).error("Error " + e.getClass() + " while trying to sgement video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to segment video " + e.getMessage()));
        }
    }


}
