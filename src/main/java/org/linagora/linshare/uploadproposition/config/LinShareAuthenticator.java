package org.linagora.linshare.uploadproposition.config;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class LinShareAuthenticator extends Authenticator {

	private final String login;
	private final String password;

	public LinShareAuthenticator(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(login, password.toCharArray());
	}
}
