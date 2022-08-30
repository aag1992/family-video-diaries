package controllers;

import com.google.inject.Inject;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import services.files.FilesManager;
import services.gcloud.GCloudAdapter;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;
import java.util.Optional;
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
            File file = filesManager.getUploadedFileFromRequest(request, "video");
            gCloudAdapter.upload(file);
            return CompletableFuture.completedFuture(ok("Succesfully uploaded file"));
        } catch (Exception e) {
            Logger.of(UploadController.class).error(e.getMessage());
            return CompletableFuture.completedFuture(ok(e.getMessage()));
        }
    }

}
