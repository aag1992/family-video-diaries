import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import services.gcloud.GCloudAdapter;
import services.gcloud.config.GCloudConfig;

public class Module extends AbstractModule {

    private final Config configuration;
    private final Environment environment;

    public Module(Environment environment, Config configuration) {
        this.configuration = configuration;
        this.environment = environment;
    }

//    @Provides
//    protected AmazonS3 s3ClientProvider() {
//        Logger.of(Module.class).error(configuration.getString("amitai.host"));
//        return new AmazonS3Client();
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
//                configuration.getString("s3.static.key.user"),
//                configuration.getString("s3.static.key.secret"));
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .withRegion(Regions.US_EAST_1)
//                .build();
//    }

    @Provides
    protected GCloudAdapter GCloudAdapter() {
        Logger.of(Module.class).error("getting cloud adapter");
        return new GCloudAdapter(new GCloudConfig(configuration));
    }

}
