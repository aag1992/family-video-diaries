package controllers;

import com.google.inject.Inject;
import model.file.upload.FileUploadDetails;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.files.FilesManager;
import services.cloud.gcloud.GCloudAdapter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UploadController extends Controller {

    private final GCloudAdapter gCloudAdapter;
    private final FilesManager filesManager;

    @Inject
    public UploadController(GCloudAdapter gCloudAdapter, FilesManager filesManager) {
        this.gCloudAdapter = gCloudAdapter;
        this.filesManager = filesManager;
    }

    @BodyParser.Of(value = BodyParser.MultipartFormData.class)
    public CompletionStage<Result> uploadVideo(Http.Request request) {
        try {
            FileUploadDetails uploadDetails = filesManager.getUploadDetailsFromRequest(request, "video");
            gCloudAdapter.upload(uploadDetails);
            return CompletableFuture.completedFuture(ok("Succesfully uploaded file"));
        } catch (Exception e) {
            Logger.of(UploadController.class).error("Error " + e.getClass() + " while trying to upload video " + e.getMessage());
            return CompletableFuture.completedFuture(internalServerError("Error " + e.getClass() + " while trying to upload video " + e.getMessage()));
        }
    }

}
