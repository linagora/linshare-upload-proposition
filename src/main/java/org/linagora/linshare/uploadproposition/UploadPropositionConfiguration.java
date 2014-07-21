package org.linagora.linshare.uploadproposition;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadPropositionConfiguration extends Configuration {
	
    @Valid
    @NotNull
    private LinShareCoreServer server = new LinShareCoreServer();

	@JsonProperty("linshare")
    public LinShareCoreServer getServer() {
		return server;
	}

	@JsonProperty("linshare")
	public void setServer(LinShareCoreServer server) {
		this.server = server;
	}
}
