package org.linagora.linshare.uploadproposition.config;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadPropositionConfiguration extends Configuration {
	
    @Valid
    @NotNull
    private LinShareCoreServer server = new LinShareCoreServer();
    
    private RecaptchaConfig captcha = new RecaptchaConfig();

	@JsonProperty("linshare")
    public LinShareCoreServer getServer() {
		return server;
	}

	@JsonProperty("linshare")
	public void setServer(LinShareCoreServer server) {
		this.server = server;
	}

	@JsonProperty("captcha")
	public RecaptchaConfig getCaptcha() {
		return captcha;
	}

	@JsonProperty("captcha")
	public void setCaptcha(RecaptchaConfig captcha) {
		this.captcha = captcha;
	}
	
}
