package org.linagora.linshare.uploadproposition.enums;

public enum ActionType {

	ACCEPT, REJECT, MANUAL;

	public static ActionType fromString(String s) {
		return ActionType.valueOf(s.toUpperCase());
	}
}
