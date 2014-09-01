package org.linagora.linshare.uploadproposition.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.jetty.http.HttpStatus;
import org.linagora.linshare.uploadproposition.config.LinShareCoreServer;
import org.linagora.linshare.uploadproposition.config.RecaptchaConfig;
import org.linagora.linshare.uploadproposition.core.LinShareError;
import org.linagora.linshare.uploadproposition.core.UploadProposition;
import org.linagora.linshare.uploadproposition.core.UploadPropositionFilter;
import org.linagora.linshare.uploadproposition.core.UploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.sun.jersey.api.Responses;
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

	private final RecaptchaConfig captcha;

	public UploadPropositionResource(Client client, LinShareCoreServer server,
			RecaptchaConfig captcha) {
		this.client = client;
		this.server = server;
		this.captcha = captcha;
	}

	@GET
	@Timed(name = "get-uploadpropositions")
	public String status() {
		return "Ok.";
	}

	@POST
	@Timed(name = "post-uploadpropositions")
	public Response create(@Valid UploadProposition proposition,
			@Context HttpServletRequest httpRequest) {
		logger.debug("input  proposition : " + proposition.toString());

		String privateKey = captcha.getPrivateKey();
		if (privateKey != null) {
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setRecaptchaServer(ReCaptchaImpl.HTTPS_SERVER);
			reCaptcha.setPrivateKey(privateKey);
			String challenge = proposition.getCaptcha_challenge();
			String response = proposition.getCaptcha_response();
			// it seems that recaptcha does not care about remote address.
			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer("42",
					challenge, response);
			if (!reCaptchaResponse.isValid()) {
				logger.warn("Captcha failed.");
				ResponseBuilder error = Responses.clientError();
				error.entity(new LinShareError(1000, "Captcha failed."));
				return error.build();
			}
		}

		UploadRequest req = new UploadRequest(proposition);
		if (checkAndApply(req)) {
			if (checkRecipient(req)) {
				req.setRecipientDomain(server.getDomain());
				logger.debug("pre accepted proposition : " + req.toString());
				post(req);
				logger.info("one proposition was created.");
				proposition.setCaptcha_challenge(null);
				proposition.setCaptcha_response(null);
				return Response.status(HttpStatus.OK_200).entity(proposition).build();
			}
		}
		logger.debug("rejected proposition : " + req.toString());
		logger.info("one proposition was rejected.");
		proposition.setCaptcha_challenge(null);
		proposition.setCaptcha_response(null);
		return Response.status(HttpStatus.OK_200).entity(proposition).build();
	}

	public boolean getFiltersIsOK() {
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

	private boolean checkRecipient(UploadRequest req) {
		WebResource defaultWr = client.resource(server.getUrl());
		WebResource filtersWr = defaultWr.path("recipients/"+req.getRecipientMail());
		ClientResponse response = filtersWr
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.get(ClientResponse.class);
		logger.debug(response.toString());
		if (response.getStatus() == 204) {
			return true;
		}
		logger.error("recipient not found : " + req.getRecipientMail());
		return false;
	}
}
