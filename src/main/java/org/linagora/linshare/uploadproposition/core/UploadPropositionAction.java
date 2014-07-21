package org.linagora.linshare.uploadproposition.core;

import org.hibernate.validator.constraints.NotEmpty;

public class UploadPropositionAction {

	@NotEmpty
	protected String uuid;

	@NotEmpty
	protected String action;

	protected String data;

	public UploadPropositionAction() {
		super();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UploadPropositionAction [uuid=" + uuid + ", action="
				+ action + ", data=" + data + "]";
	}
}
