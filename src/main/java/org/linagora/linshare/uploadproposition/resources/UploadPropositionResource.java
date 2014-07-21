package org.linagora.linshare.uploadproposition.resources;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.linagora.linshare.uploadproposition.LinShareCoreServer;
import org.linagora.linshare.uploadproposition.core.UploadProposition;
import org.linagora.linshare.uploadproposition.core.UploadPropositionFilter;
import org.linagora.linshare.uploadproposition.core.UploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

@Path("/uploadpropositions")
@Produces(MediaType.APPLICATION_JSON)
public class UploadPropositionResource {
	private static final Logger logger = LoggerFactory
			.getLogger(UploadPropositionResource.class);

	private final Client client;

	private final LinShareCoreServer server;

	public UploadPropositionResource(Client client, LinShareCoreServer server) {
		this.client = client;
		this.server = server;
	}

	@GET
	@Timed(name = "get-uploadpropositions")
	public String status() {
		return "Ok.";
	}

	@POST
	@Timed(name = "post-uploadpropositions")
	public UploadProposition create(@Valid UploadProposition proposition) {
		logger.debug("input  proposition : " + proposition.toString());

		// TODO check the captcha.
		UploadRequest req = new UploadRequest(proposition);

		if (checkAndApply(req)) {
			req.setRecipientDomain(server.getDomain());
			logger.debug("pre accepted proposition : " + req.toString());
			post(req);
			logger.info("one proposition was created.");
		} else {
			logger.debug("rejected proposition : " + req.toString());
			logger.info("one proposition was rejected.");
		}
		return proposition;
	}

	public boolean getFiltersIsOK(){
		try {
			List<UploadPropositionFilter> filters = getFilters();
			if (filters != null) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private List<UploadPropositionFilter> getFilters() {
		WebResource defaultWr = client.resource(server.getUrl());
		WebResource filtersWr = defaultWr.path("filters");
		ClientResponse response = filtersWr
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.get(ClientResponse.class);
		logger.debug(response.toString());
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		String jsonStream = response.getEntity(String.class);
		logger.debug("jsonStream" + jsonStream);
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<UploadPropositionFilter> filters = mapper.readValue(
					jsonStream,
					new TypeReference<List<UploadPropositionFilter>>() {
					});
			logger.debug(filters.toString());
			return filters;
		} catch (JsonGenerationException e) {
			throw new RuntimeException("Failed : JsonGenerationException : "
					+ e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Failed : JsonGenerationException : "
					+ e);
		} catch (IOException e) {
			throw new RuntimeException("Failed : JsonGenerationException : "
					+ e);
		}
	}

	private boolean checkAndApply(UploadRequest req) {
		List<UploadPropositionFilter> filters = getFilters();
		for (UploadPropositionFilter filter : filters) {
			if (filter.match(req)) {
				filter.setAction(req);
				if (server.isSendRejected()) {
					return true;
				}
				return !req.isRejected();
			}
		}
		return true;
	}

	private void post(UploadRequest req) {
		WebResource defaultWr = client.resource(server.getUrl());
		WebResource filtersWr = defaultWr.path("propositions");
		Builder builder = filtersWr.accept(MediaType.APPLICATION_JSON_TYPE);
		builder.type(MediaType.APPLICATION_JSON_TYPE);
		builder.post(req);
	}
}
