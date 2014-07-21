package org.linagora.linshare.uploadproposition.core;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.collect.Lists;

public class UploadPropositionFilter {

	@NotEmpty
	protected String uuid;

	@NotEmpty
	protected String name;

	@NotEmpty
	protected boolean matchAll;

	@Valid
	protected List<UploadPropositionRule> uploadPropositionRules = Lists
			.newArrayList();

	@Valid
	protected List<UploadPropositionAction> uploadPropositionActions = Lists
			.newArrayList();

	public UploadPropositionFilter() {
		super();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMatchAll() {
		return matchAll;
	}

	public void setMatchAll(boolean matchAll) {
		this.matchAll = matchAll;
	}

	public List<UploadPropositionRule> getUploadPropositionRules() {
		return uploadPropositionRules;
	}

	public void setUploadPropositionRules(
			List<UploadPropositionRule> uploadPropositionRules) {
		this.uploadPropositionRules = uploadPropositionRules;
	}

	public List<UploadPropositionAction> getUploadPropositionActions() {
		return uploadPropositionActions;
	}

	public void setUploadPropositionActions(
			List<UploadPropositionAction> uploadPropositionActions) {
		this.uploadPropositionActions = uploadPropositionActions;
	}

	@Override
	public String toString() {
		return "UploadPropositionFilter [uuid=" + uuid + ", name=" + name
				+ ", matchAll=" + matchAll + ", uploadPropositionRules="
				+ uploadPropositionRules + ", uploadPropositionActions="
				+ uploadPropositionActions + "]";
	}

	public boolean match(UploadRequest req) {
		int successCpt = 0;
		for (UploadPropositionRule rule : uploadPropositionRules) {
			if (rule.match(req)) {
				if (!isMatchAll()) {
					// only one match is enough
					return true;
				}
				successCpt += 1;
			}
		}
		// at least one rule matched
		if (successCpt >= 1) {
			// all rules should match.
			if (uploadPropositionRules.size() == successCpt) {
				return true;
			}
		}
		return false;
	}

	public void setAction(UploadRequest req) {
		List<UploadPropositionAction> actions = this.getUploadPropositionActions();
		for (UploadPropositionAction action : actions) {
			// should be only one action.
			req.setAction(action.getAction());
			break;
		}
	}
}
