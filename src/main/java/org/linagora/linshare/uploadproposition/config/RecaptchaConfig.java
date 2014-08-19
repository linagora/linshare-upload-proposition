package org.linagora.linshare.uploadproposition.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecaptchaConfig {

	protected String privateKey;

	public RecaptchaConfig() {
		super();
	}

	@JsonProperty
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
}
