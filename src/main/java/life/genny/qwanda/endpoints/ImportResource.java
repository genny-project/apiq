package life.genny.qwanda.endpoints;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonValue;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.identity.SecurityIdentity;
import life.genny.notes.utils.LocalDateTimeAdapter;
import life.genny.qwanda.GennyToken;
import life.genny.qwanda.attribute.Attribute;
import life.genny.qwanda.attribute.EntityAttribute;
import life.genny.qwanda.datatype.DataType;
import life.genny.qwanda.entity.BaseEntity;
import life.genny.qwanda.entity.EntityEntity;
import life.genny.qwanda.message.QDataAttributeMessage;
import life.genny.qwanda.message.QDataBaseEntityMessage;
import life.genny.qwanda.validation.Validation;
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

	@Inject
	UserTransaction userTransaction;

	@OPTIONS
	public Response opt() {
		return Response.ok().build();
	}

	@GET
	@Path("/attributes")
	@Transactional
	public Response importAttributes(@Context UriInfo uriInfo, @QueryParam("url") String externalGennyUrl) {
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

	@GET
	@Path("/baseentitys")
	@Transactional
	//@TransactionConfiguration()
	public Response importBaseentitys(@Context UriInfo uriInfo, @QueryParam("url") String externalGennyUrl) {
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
			String hql = "select ea from EntityAttribute ea";
			hql = Base64.getUrlEncoder().encodeToString(hql.getBytes());
			jsonString = QwandaUtils.apiGet(externalGennyUrl + "/qwanda/baseentitys/search23/" + hql + "/0/10000000",
					accessToken.getRawToken());

			if (jsonString.contains("404 Not Found") || jsonString.startsWith("<html>")) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			if (!StringUtils.isBlank(jsonString)) {
				JsonObject jsonMsg = new Gson().fromJson(jsonString, JsonObject.class);
				JsonArray items = jsonMsg.getAsJsonArray("items");

//				JsonParser parser = new JsonParser();
//				JsonElement tradeElement = parser.parse(jsonString);
//				JsonArray attributeValues = tradeElement.getAsJsonArray();
//
//				try {
//					userTransaction.setTransactionTimeout(5600);
//					userTransaction.begin();

					/* we loop through the attribute values */
					for (int i = 0; i < items.size(); i++) {

						BaseEntity be = new BaseEntity();

						JsonObject beJson = items.get(i).getAsJsonObject();
						String code = beJson.get("code").getAsString();
						LocalDateTime created = ImportResource.getLocalDateTime(beJson.get("created"));
						LocalDateTime updated = ImportResource.getLocalDateTime(beJson.get("updated"));
						String name = beJson.get("name").getAsString();

						be.code = code;
						be.name = name;
						be.created = created;
						be.updated = updated;
						be.persist();

						JsonArray bes = beJson.getAsJsonArray("baseEntityAttributes");

						for (int e = 0; e < bes.size(); e++) {
							JsonObject ea = bes.get(e).getAsJsonObject();
							String attributeCode = ea.get("attributeCode").getAsString();
							String attributeName = ea.get("attributeName").getAsString();
							Boolean readonly = ea.get("readonly").getAsBoolean();
							Boolean inferred = ea.get("inferred").getAsBoolean();
							Boolean privacyFlag = ea.get("privacyFlag").getAsBoolean();
							Double weight = ea.get("weight").getAsDouble();
							LocalDateTime createdEA = ImportResource.getLocalDateTime(ea.get("created"));
							LocalDateTime updatedEA = ImportResource.getLocalDateTime(ea.get("updated"));
							// String valueString =
							EntityAttribute eat = new EntityAttribute();
							eat.attributeCode = attributeCode;
							eat.attributeName = attributeName;
							Attribute attribute = Attribute.find("code", attributeCode).firstResult();
							eat.attribute = attribute;
							eat.readonly = readonly;
							eat.inferred = inferred;
							eat.privacyFlag = privacyFlag;
							eat.setWeight(weight);
							eat.created = createdEA;
							eat.updated = updatedEA;
							eat.baseentity = be;
							eat.baseEntityCode = be.code;
							eat.persist();

						}

						JsonArray links = beJson.getAsJsonArray("links");
						for (int link = 0; link < links.size(); link++) {
							JsonObject l = links.get(link).getAsJsonObject();
							
							JsonObject lnk = l.getAsJsonObject("link");
							String attributeCode = lnk.get("attributeCode").getAsString();
							String targetCode = lnk.get("targetCode").getAsString();
							String sourceCode = lnk.get("sourceCode").getAsString();
							String linkValue = lnk.get("linkValue").getAsString();
							Double weight = lnk.get("weight").getAsDouble();
							
							LocalDateTime createdEE = ImportResource.getLocalDateTime(l.get("created"));
							String valueStringEE = l.get("valueString").getAsString();
							Double weightEE = l.get("weight").getAsDouble();
							
							EntityEntity ee = new EntityEntity();
							Attribute attribute = Attribute.find("code", attributeCode).firstResult();
							ee.attribute = attribute;
							ee.attributeCode = attributeCode;
							ee.sourceCode = sourceCode;
							ee.targetCode = targetCode;
							ee.created = createdEE;
							ee.value.dataType = attribute.dataType;
							ee.value.valueString = linkValue;
							ee.value.weight = weight;
							
							ee.link.attributeCode = attributeCode;
							ee.link.linkValue = linkValue;
							ee.link.sourceCode = sourceCode;
							ee.link.targetCode = targetCode;
							ee.link.weight = weight;

							ee.persist();

						}

						JsonArray questions = beJson.getAsJsonArray("questions");

						for (int q = 0; q < questions.size(); q++) {
							JsonObject question = questions.get(q).getAsJsonObject();

						}

						System.out.println(code);
					}
//					userTransaction.commit();
//
//				} catch (SystemException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (NotSupportedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (RollbackException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (HeuristicMixedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (HeuristicRollbackException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

//				QDataBaseEntityMessage beMsg = JsonUtils.fromJson(jsonString, QDataBaseEntityMessage.class);
//				BaseEntity[] beArray = beMsg.getItems();

//				jsonString = fixJson(jsonString);
//				JsonArray jsonBEs = new Gson().fromJson(jsonString, JsonArray.class);

			}

			return Response.status(Status.OK).build();
		} catch (IOException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	private String fixJson(String resultStr) {
		String resultStr2 = resultStr.replaceAll(Pattern.quote("\\\""), Matcher.quoteReplacement("\""));
		String resultStr3 = resultStr2.replaceAll(Pattern.quote("\\n"), Matcher.quoteReplacement("\n"));
		String resultStr4 = resultStr3.replaceAll(Pattern.quote("\\\n"), Matcher.quoteReplacement("\n"));
//		String resultStr5 = resultStr4.replaceAll(Pattern.quote("\"{"),
//				Matcher.quoteReplacement("{"));
//		String resultStr6 = resultStr5.replaceAll(Pattern.quote("\"["),
//				Matcher.quoteReplacement("["));
//		String resultStr7 = resultStr6.replaceAll(Pattern.quote("]\""),
//				Matcher.quoteReplacement("]"));
//		String resultStr8 = resultStr5.replaceAll(Pattern.quote("}\""), Matcher.quoteReplacement("}"));
		String ret = resultStr4.replaceAll(Pattern.quote("\\\"" + ""), Matcher.quoteReplacement("\""));
		return ret;

	}

	@Transactional
	void onStart(@Observes StartupEvent ev) {
		log.info("Import Endpoint starting");

	}

	@Transactional
	void onShutdown(@Observes ShutdownEvent ev) {
		log.info("Import Endpoint Shutting down");
	}

	static public LocalDateTime getLocalDateTime(JsonElement jsonElement) {
		if (jsonElement == null) {
			return null;
		}
		String str = jsonElement.getAsString();
		String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
		// str = str.substring(0,str.length()-1);
		String value = str;// .substring(1);
		LocalDateTime ret = null;

		try {
			ret = dateFormatter.parse(value, LocalDateTime::from);
		} catch (Exception e) {
			dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SS";
			dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
			try {
				ret = dateFormatter.parse(value, LocalDateTime::from);
			} catch (Exception ee) {
				dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.S";
				dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
				try {
					ret = dateFormatter.parse(value, LocalDateTime::from);
				} catch (Exception eee) {
					dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";
					dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
					ret = dateFormatter.parse(value, LocalDateTime::from);
				}
			}

		}

		return ret;
	}

}