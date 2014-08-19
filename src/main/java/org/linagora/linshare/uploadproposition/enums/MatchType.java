package org.linagora.linshare.uploadproposition.enums;

public enum MatchType {
	ALL, ANY, TRUE;

	public static MatchType fromString(String s) {
		return MatchType.valueOf(s.toUpperCase());
	}
}
