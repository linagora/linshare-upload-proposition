package org.linagora.linshare.uploadproposition.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinShareCoreServer {

	@NotEmpty
	private String url;

	@NotEmpty
	private String login;

	@NotEmpty
	private String password;

	private boolean sendRejected;

	private String domain;

	public LinShareCoreServer() {
		super();
	}

	@JsonProperty
	public String getUrl() {
		return url;
	}

	@JsonProperty
	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isSendRejected() {
		return sendRejected;
	}

	public void setSendRejected(boolean sendRejected) {
		this.sendRejected = sendRejected;
	}
}
