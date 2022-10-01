import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import play.Environment;
import play.Logger;
import services.cloud.gcloud.GCloudAdapter;
import services.cloud.gcloud.config.GCloudConfig;

public class Module extends AbstractModule {

    private final Config configuration;
    private final Environment environment;

    public Module(Environment environment, Config configuration) {
        this.configuration = configuration;
        this.environment = environment;
    }
    @Provides
    protected GCloudAdapter GCloudAdapter() {
        Logger.of(Module.class).info("getting cloud adapter");
            return new GCloudAdapter(new GCloudConfig(configuration));
    }

}
