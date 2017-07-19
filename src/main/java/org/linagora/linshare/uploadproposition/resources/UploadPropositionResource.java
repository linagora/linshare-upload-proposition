package org.linagora.linshare.uploadproposition.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

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

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

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
				ResponseBuilder serverError = Response.serverError();
				serverError.status(Status.BAD_REQUEST);
				serverError.entity(new LinShareError(1000, "Captcha failed."));
				return serverError.build();
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
			e.printStackTrace();
		}
		return false;
	}

	private List<UploadPropositionFilter> getFilters() {
		WebTarget target = client.target(server.getUrl()).path("filters");
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get();
		logger.debug(response.toString());
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		GenericType<List<UploadPropositionFilter>> responseType = new GenericType<List<UploadPropositionFilter>>(){};
		List<UploadPropositionFilter> filters= response.readEntity(responseType);
		logger.debug(filters.toString());
		return filters;
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
		Entity<UploadRequest> json = Entity.json(req);
		WebTarget target = client.target(server.getUrl()).path("propositions");
		target.request(MediaType.APPLICATION_JSON_TYPE)
			.accept(MediaType.APPLICATION_JSON_TYPE)
			.post(json);
	}

	private boolean checkRecipient(UploadRequest req) {
		WebTarget target = client.target(server.getUrl()).path("recipients/" + req.getRecipientMail());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get();
		logger.debug(response.toString());
		if (response.getStatus() == 204) {
			return true;
		}
		logger.error("recipient not found : " + req.getRecipientMail());
		return false;
	}
}
