package com.hex.ml.logistic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;

@Path("/sgd")
public class SgdService extends Application{
	@GET
	@Path("generate-model")
	@Produces(MediaType.TEXT_PLAIN)
	public String generateModel() {
		return "Success";
	}
}
