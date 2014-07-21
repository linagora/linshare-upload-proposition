package org.linagora.linshare.uploadproposition.core;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadPropositionRule {

	private static final Logger logger = LoggerFactory
			.getLogger(UploadPropositionRule.class);

	@NotEmpty
	protected String uuid;

	@NotEmpty
	protected String operator;

	@NotEmpty
	protected String field;

	protected String value;

	public UploadPropositionRule() {
		super();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "UploadPropositionRule [uuid=" + uuid + ", operator=" + operator
				+ ", field=" + field + ", value=" + value + "]";
	}

	public boolean match(UploadRequest req) {
		if (this.operator.equals("TRUE")) {
			return true;
		}
		String compare = null;
		switch (this.field) {
		case "SENDER_EMAIL":
			compare = req.getMail();
			break;
		case "RECIPIENT_EMAIL":
			compare = req.getRecipientMail();
			break;
		case "RECIPIENT_DOMAIN":
			// To be implemented.
			return false;
		case "SUBJECT":
			compare = req.getSubject();
			break;
		default:
			logger.error("Unknown field " + this.field + " on rule : " + uuid);
			return false;
		}
		if (compare == null) {
			return false;
		}
		if (value == null) {
			return false;
		}
		switch (this.operator) {
		case "CONTAIN":
			return value.contains(compare);
		case "DO_NOT_CONTAIN":
			return !value.contains(compare);
		case "EQUAL":
			return value.equals(compare);
		case "DO_NOT_EQUAL":
			return !value.equals(compare);
		case "BEGIN_WITH":
			return value.startsWith(compare);
		case "END_WITH":
			return value.endsWith(compare);
		default:
			logger.error("Unknown operator " + this.operator + " on rule : " + uuid);
			return false;
		}
	}
}
