package org.linagora.linshare.uploadproposition.core;

import org.hibernate.validator.constraints.NotEmpty;

public class UploadProposition {

	@NotEmpty
	protected String subject;

	@NotEmpty
	protected String body;

	@NotEmpty
	protected String mail;

	@NotEmpty
	protected String firstName;

	@NotEmpty
	protected String lastName;

	@NotEmpty
	protected String recipientMail;

	protected String captcha_response;

	protected String captcha_challenge;


	public UploadProposition() {
		super();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRecipientMail() {
		return recipientMail;
	}

	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

	public String getCaptcha_response() {
		return captcha_response;
	}

	public void setCaptcha_response(String captcha_response) {
		this.captcha_response = captcha_response;
	}

	public String getCaptcha_challenge() {
		return captcha_challenge;
	}

	public void setCaptcha_challenge(String captcha_challenge) {
		this.captcha_challenge = captcha_challenge;
	}

	@Override
	public String toString() {
		return "UploadProposition [subject=" + subject + ", body=" + body
				+ ", mail=" + mail + ", firstName=" + firstName + ", lastName="
				+ lastName + ", recipientMail=" + recipientMail
				+ ", captcha_response=" + captcha_response
				+ ", captcha_challenge=" + captcha_challenge + "]";
	}
}
