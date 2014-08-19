package org.linagora.linshare.uploadproposition.core;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.linagora.linshare.uploadproposition.enums.MatchType;

import com.google.common.collect.Lists;

public class UploadPropositionFilter {

	@NotEmpty
	protected String uuid;

	@NotEmpty
	protected String name;

	@NotEmpty
	protected String match;

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

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
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
				+ ", match=" + match + ", uploadPropositionRules="
				+ uploadPropositionRules + ", uploadPropositionActions="
				+ uploadPropositionActions + "]";
	}

	public boolean match(UploadRequest req) {
		int successCpt = 0;
		MatchType matchType = MatchType.fromString(this.match);
		if (matchType.equals(MatchType.TRUE)) {
			return true;
		}
		for (UploadPropositionRule rule : uploadPropositionRules) {
			if (rule.match(req)) {
				if (matchType.equals(MatchType.ANY)) {
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
