package org.linagora.linshare.uploadproposition.health;

import org.linagora.linshare.uploadproposition.resources.UploadPropositionResource;

import com.codahale.metrics.health.HealthCheck;

public class LinShareCoreCheck extends HealthCheck {
    
    private UploadPropositionResource propositionResource;
    
    public LinShareCoreCheck(UploadPropositionResource propositionResource) {
		super();
		this.propositionResource = propositionResource;
	}

	@Override
    protected Result check() throws Exception {
		if (propositionResource.getFiltersIsOK()) {
			return Result.healthy();
		}
		return Result.unhealthy("LinShare core is not available.");
    }
}
