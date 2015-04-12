package com.hex.ml.logistic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sgd")
public class SgdService {
	@GET
	@Path("generateModel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateModel(@QueryParam("inputLoc") String inputLoc,
			@QueryParam("targetClass") String targetClass,
			@QueryParam("varType") String varType,@QueryParam("header") String header) {
		SGDMLP sgd = new SGDMLP();
		String resultJson="";
		try {
			resultJson=sgd.runSGD(inputLoc,targetClass,varType,header);
			System.out.println(resultJson);
		} catch (Exception e) {
			Response.serverError();
		}		
		
		Response response = Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
		return response;
	}
}
