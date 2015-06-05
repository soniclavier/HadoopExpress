package com.hex.ml.logistic;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hex.ml.utility.DataJson;

@Path("/sgd")
public class SgdService {
	@GET
	@Path("generateModel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateModel(@QueryParam("inputLoc") String inputLoc,
			@QueryParam("targetClass") String targetClass,
			@QueryParam("varType") String varType,
			@QueryParam("header") String header) {
		SGDMLP sgd = new SGDMLP();
		String resultJson = "";
		try {
			resultJson = sgd.runSGD(inputLoc, targetClass, varType, header);

			// dummy code.. delete
			/*
			 * JSONArray features = new JSONArray();
			 * 
			 * 
			 * JSONObject dummy = new JSONObject();
			 * dummy.append("name","Sepal Color"); JSONObject dummyCatType = new
			 * JSONObject(); dummyCatType.append("varType","categorical");
			 * dummyCatType.append("values", "green,yellow");
			 * dummy.append("type",dummyCatType);
			 * 
			 * JSONObject dummy2 = new JSONObject();
			 * dummy2.append("name","Sepal Width"); JSONObject dummyNumType =
			 * new JSONObject(); dummyNumType.append("varType","numeric");
			 * dummyNumType.append("min", "10"); dummyNumType.append("max",
			 * "100"); dummy2.append("type",dummyNumType);
			 * 
			 * features.put(dummy); features.put(dummy2); JSONObject result =
			 * new JSONObject(); result.append("success","dummy");
			 * result.append("features", features); resultJson =
			 * result.toString();
			 */

			System.out.println(resultJson);
		} catch (Exception e) {
			Response.serverError();
		}

		Response response = Response.ok(resultJson, MediaType.APPLICATION_JSON)
				.build();
		return response;
	}

	@POST
	@Path("predict")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	public Response predict(MultivaluedMap<String, String> formParams) {
		for (String key : formParams.keySet()) {
			System.out.println(key);
			System.out.println(formParams.get(key).get(0));
		}
		Response response = Response.ok("{}", MediaType.APPLICATION_JSON)
				.build();
		return response;
	}

	@GET
	@Path("loadFile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadFile(@QueryParam("inputLoc") String inputLoc,
			@QueryParam("headerIncluded") String headerChckbox) {
		boolean headerIncluded = Boolean.parseBoolean(headerChckbox);
		if (headerIncluded) {
			try {
				String header = DataJson.getHeaders(inputLoc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Response response = Response.ok("{}", MediaType.APPLICATION_JSON)
				.build();
		return response;
	}
	
	@GET
	@Path("loadHeader")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadHeader(@QueryParam("inputLoc") String inputLoc) {
		String header = "";
		try {
			header = DataJson.getHeaders(inputLoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		JSONObject headerJSON = new JSONObject();
		try {
			headerJSON.put("header",header);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Response response = Response.ok(headerJSON.toString(), MediaType.APPLICATION_JSON)
				.build();
		return response;
	}

}
