package life.genny.qwanda.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.identity.SecurityIdentity;
import life.genny.notes.models.GennyToken;
import life.genny.qwanda.attribute.Attribute;
import life.genny.qwanda.datatype.DataType;
import life.genny.qwanda.message.QDataAttributeMessage;
import life.genny.qwanda.validation.Validation;
import life.genny.qwanda.validation.ValidationList;
import life.genny.qwandautils.JsonUtils;
import life.genny.qwandautils.QwandaUtils;

@Path("/import")
@RegisterForReflection
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportResource {

	private static final Logger log = Logger.getLogger(ImportResource.class);

	@ConfigProperty(name = "default.realm", defaultValue = "genny")
	String defaultRealm;

	@Inject
	SecurityIdentity securityIdentity;

	@Inject
	JsonWebToken accessToken;

	@OPTIONS
	public Response opt() {
		return Response.ok().build();
	}

	@GET
	@Path("/attributes")
	@Transactional
	public Response importAttributes(@Context UriInfo uriInfo, @QueryParam("url") String  externalGennyUrl) {
		GennyToken userToken = new GennyToken(accessToken.getRawToken());
		if (userToken == null) {
			return Response.status(Status.FORBIDDEN).build();
		}

		if (!userToken.hasRole("dev") && !userToken.hasRole("superadmin")) {
			throw new WebApplicationException("User not recognised. Entity not being created", Status.FORBIDDEN);
		}

		log.info("External Genny Url = " + externalGennyUrl);
		String jsonString;
		try {
			jsonString = QwandaUtils.apiGet(externalGennyUrl + "/qwanda/attributes", accessToken.getRawToken());
			if (!StringUtils.isBlank(jsonString)) {

				QDataAttributeMessage attributesMsg = JsonUtils.fromJson(jsonString, QDataAttributeMessage.class);
				Attribute[] attributeArray = attributesMsg.getItems();

				for (Attribute attribute : attributeArray) {
					log.info(attribute);
					Attribute existing = Attribute.find("code", attribute.code).firstResult();
					
					DataType dt = attribute.dataType;
					List<Validation> vl = dt.getValidationList();
					List<Validation> goodList = new ArrayList<Validation>();
					for (Validation v : vl) {
						Validation existingValidation = Validation.find("code", v.code).firstResult();
						if (existingValidation == null) {
							v.persist();
							goodList.add(v);
						} else {
							goodList.add(existingValidation);
						}
					}
					
					
					
					if (existing == null) {
						attribute.id = null;
						attribute.dataType.setValidationList(goodList);
						attribute.persist();
					}
				}
			}

			return Response.status(Status.OK).build();
		} catch (IOException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	@Transactional
	void onStart(@Observes StartupEvent ev) {
		log.info("Import Endpoint starting");

	}

	@Transactional
	void onShutdown(@Observes ShutdownEvent ev) {
		log.info("Import Endpoint Shutting down");
	}
}