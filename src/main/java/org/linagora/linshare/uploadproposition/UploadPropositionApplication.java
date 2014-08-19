package org.linagora.linshare.uploadproposition;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.Authenticator;

import org.linagora.linshare.uploadproposition.config.LinShareAuthenticator;
import org.linagora.linshare.uploadproposition.config.UploadPropositionConfiguration;
import org.linagora.linshare.uploadproposition.health.LinShareCoreCheck;
import org.linagora.linshare.uploadproposition.resources.UploadPropositionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class UploadPropositionApplication extends Application<UploadPropositionConfiguration> {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UploadPropositionApplication.class);
	
	
    public static void main(String[] args) throws Exception {
        new UploadPropositionApplication().run(args);
    }

    @Override
    public String getName() {
        return "LinShare-UploadProposition";
    }

    @Override
    public void initialize(Bootstrap<UploadPropositionConfiguration> bootstrap) {
    }

    @Override
    public void run(UploadPropositionConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        Authenticator.setDefault(new LinShareAuthenticator(configuration.getServer().getLogin(), configuration.getServer().getPassword()));
        // Little dirty. DropWizard Jersey client did not work at the time.
        ClientConfig cc = new DefaultClientConfig();
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(cc);
        UploadPropositionResource propositionResource = new UploadPropositionResource(client, configuration.getServer(), configuration.getCaptcha());
        environment.healthChecks().register("LinShare-Core", new LinShareCoreCheck(propositionResource));
		environment.jersey().register(propositionResource);
    }
}
