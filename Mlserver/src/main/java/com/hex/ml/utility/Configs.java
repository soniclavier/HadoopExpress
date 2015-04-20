package com.hex.ml.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/config")
public class Configs {

	private static Configs instance = null;
	private String fileName = "res/config.properties";
	private Properties prop = new Properties();

	public Configs() throws IOException {
		InputStream input = new FileInputStream(fileName);
		prop.load(input);
		input.close();
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public synchronized void setProperty(String key, String value)
			throws IOException {
		prop.setProperty(key, value);
		OutputStream out = new FileOutputStream(fileName);
		prop.store(out, "");
		out.close();
	}

	public static Configs getInstance() {
		if (instance == null) {
			try {
				instance = new Configs();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() throws JSONException {
		JSONObject list = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Object key : prop.keySet()) {
			String keyStr = (String) key;
			JSONObject entry = new JSONObject();
			String value = prop.getProperty(keyStr);
			entry.put("key", keyStr);
			entry.put("value", value);
			arr.put(entry);
		}
		list.put("configs", arr);
		Response response = Response.ok(list.toString(),
				MediaType.APPLICATION_JSON).build();
		return response;
	}

	@POST
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	public Response predict(MultivaluedMap<String, String> formParams)
			throws IOException {
		for (String key : formParams.keySet()) {
			prop.setProperty(key, formParams.get(key).get(0));
		}
		OutputStream out = new FileOutputStream(fileName);
		prop.store(out, "");
		out.close();

		Response response = Response.ok("{}", MediaType.APPLICATION_JSON)
				.build();
		return response;
	}

	public static void main(String[] args) throws IOException {
		Configs conf = new Configs();
		System.out.println(conf.getProperty("testkey"));
		conf.setProperty("testkey", "neww2");
		System.out.println(conf.getProperty("testkey"));

	}

}
