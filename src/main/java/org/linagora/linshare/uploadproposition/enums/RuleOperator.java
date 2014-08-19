package org.linagora.linshare.uploadproposition.enums;

public enum RuleOperator {

	CONTAIN {
		@Override
		public boolean check(String value, String compare) {
			return value.contains(compare);
		}
	},
	DO_NOT_CONTAIN {
		@Override
		public boolean check(String value, String compare) {
			return !value.contains(compare);
		}
	},
	EQUAL {
		@Override
		public boolean check(String value, String compare) {
			return value.equals(compare);
		}
	},
	DO_NOT_EQUAL {
		@Override
		public boolean check(String value, String compare) {
			return !value.equals(compare);
		}
	},
	BEGIN_WITH {
		@Override
		public boolean check(String value, String compare) {
			return value.startsWith(compare);
		}
	},
	END_WITH {
		@Override
		public boolean check(String value, String compare) {
			return value.endsWith(compare);
		}
	};

	public static RuleOperator fromString(String s) {
		return RuleOperator.valueOf(s.toUpperCase());
	}

	public abstract boolean check(String value, String compare);
}
