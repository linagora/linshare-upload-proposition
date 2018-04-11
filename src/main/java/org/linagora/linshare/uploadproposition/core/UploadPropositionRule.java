package org.linagora.linshare.uploadproposition.core;

import org.hibernate.validator.constraints.NotEmpty;
import org.linagora.linshare.uploadproposition.enums.RuleField;
import org.linagora.linshare.uploadproposition.enums.RuleOperator;
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
		String compare = null;
		RuleField field = RuleField.fromString(this.field);
		switch (field) {
		case SENDER_EMAIL:
			compare = req.getMail();
			break;
		case RECIPIENT_EMAIL:
			compare = req.getRecipientMail();
			break;
		case RECIPIENT_DOMAIN:
			// To be implemented.
			return false;
		case SUBJECT:
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
		RuleOperator op = RuleOperator.fromString(this.operator);
		return op.check(compare, value);
	}
}
