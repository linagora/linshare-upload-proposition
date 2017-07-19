package org.linagora.linshare.uploadproposition;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.linagora.linshare.uploadproposition.config.UploadPropositionConfiguration;
import org.linagora.linshare.uploadproposition.health.LinShareCoreCheck;
import org.linagora.linshare.uploadproposition.resources.UploadPropositionResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class UploadPropositionApplication extends Application<UploadPropositionConfiguration> {

	public static void main(final String[] args) throws Exception {
		new UploadPropositionApplication().run(args);
	}

	@Override
	public String getName() {
		return "LinShare-UploadProposition";
	}

	@Override
	public void initialize(final Bootstrap<UploadPropositionConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
				bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor()));
	}

	@Override
	public void run(final UploadPropositionConfiguration configuration, final Environment environment) {
		HttpAuthenticationFeature authBasic = HttpAuthenticationFeature.basic(
				configuration.getServer().getLogin(),
				configuration.getServer().getPassword());
		final Client client = ClientBuilder.newBuilder()
				.register(authBasic)
				.register(JacksonFeature.class)
				.build();
		UploadPropositionResource propositionResource = new UploadPropositionResource(
				client,
				configuration.getServer(),
				configuration.getCaptcha());
		environment.jersey()
			.register(propositionResource);
		environment.healthChecks()
			.register("LinShare-Core", new LinShareCoreCheck(propositionResource));
	}

}
