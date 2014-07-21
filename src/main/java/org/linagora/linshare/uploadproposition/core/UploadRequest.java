package org.linagora.linshare.uploadproposition.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

//@JsonIgnoreProperties
public class UploadRequest {

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

	@JsonProperty
	protected String recipientDomain;

	@NotEmpty
	protected String action;

	
	
	public UploadRequest(UploadProposition proposition) {
		super();
		this.subject = proposition.getSubject();
		this.body = proposition.getBody();
		this.mail = proposition.getMail();
		this.firstName = proposition.getFirstName();
		this.lastName = proposition.getLastName();
		this.recipientMail = proposition.getRecipientMail();
	}

	public String getRecipientDomain() {
		return recipientDomain;
	}

	public void setRecipientDomain(String recipientDomain) {
		this.recipientDomain = recipientDomain;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@JsonIgnore
	public boolean isRejected() {
		return "REJECT".equals(action);
	}

	@Override
	public String toString() {
		return "UploadProposition [subject=" + subject + ", body=" + body
				+ ", mail=" + mail + ", firstName=" + firstName + ", lastName="
				+ lastName + ", recipientMail=" + recipientMail
				+ ", recipientDomain=" + recipientDomain + ", action=" + action
				+ "]";
	}
}
