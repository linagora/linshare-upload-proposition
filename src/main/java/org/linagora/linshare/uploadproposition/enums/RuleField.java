package org.linagora.linshare.uploadproposition.enums;

public enum RuleField {

	SENDER_EMAIL, RECIPIENT_EMAIL, RECIPIENT_DOMAIN, SUBJECT;

	public static RuleField fromString(String s) {
		return RuleField.valueOf(s.toUpperCase());
	}
}
