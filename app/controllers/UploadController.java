package controllers;

import com.google.inject.Inject;
import model.video.upload.VideoSegmentingDetails;
import model.video.upload.VideoUploadDetails;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.video.VideoUploadManager;
import services.cloud.gcloud.GCloudAdapter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static services.video.consts.VideoUploadConsts.*;

public class UploadController extends Controller {

    private final GCloudAdapter gCloudAdapter;
    private final VideoUploadManager videoUploadManager;

    @Inject
    public UploadController(GCloudAdapter gCloudAdapter, VideoUploadManager videoUploadManager) {
        this.gCloudAdapter = gCloudAdapter;
        this.videoUploadManager = videoUploadManager;
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> uploadVideo(Http.Request request) {
        try {
            VideoUploadDetails uploadDetails = videoUploadManager.getUploadDetailsFromRequest(request, VIDEO);
            gCloudAdapter.upload(uploadDetails);
            return CompletableFuture.completedFuture(ok("Successfully uploaded file"));
        } catch (Exception e) {
            Logger.of(UploadController.class).error("Error " + e.getClass() + " while trying to upload video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to upload video " + e.getMessage()));
        }
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> segmentVideo(Http.Request request) {
        try {
            VideoSegmentingDetails uploadDetails = videoUploadManager.getVideoSegmentingDetailsFromRequest(request, SEGMENTS);
            gCloudAdapter.get(uploadDetails);
            return CompletableFuture.completedFuture(ok("Successfully segmented video file"));
        } catch (Exception e) {
            Logger.of(UploadController.class).error("Error " + e.getClass() + " while trying to sgement video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to segment video " + e.getMessage()));
        }
    }


}
