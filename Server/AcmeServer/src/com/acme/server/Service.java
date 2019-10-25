package com.acme.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/api")
public class Service {
	
	private Connection connection = null;

	public Service() {
		try {
			connectToDatabase();
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void connectToDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		try {
			// Francisco path to db -> "jdbc:sqlite:///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/acme.db"
			
			connection = DriverManager.getConnection("jdbc:sqlite:///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/acme.db");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@GET
	@Produces("application/json")
	public Response convertFtoC() throws JSONException {
		
		if(connection == null) {
			String result = "NOT CONNECTED TO DB";
			return Response.status(200).entity(result).build();
		}
 
		JSONObject jsonObject = new JSONObject();
		Double fahrenheit = 98.24;
		Double celsius;
		celsius = (fahrenheit - 32) * 5 / 9;
		jsonObject.put("F Value", fahrenheit);
		jsonObject.put("C Value", celsius);
 
		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}
 
	@Path("{f}")
	@GET
	@Produces("application/json")
	public Response convertFtoCfromInput(@PathParam("f") float f) throws JSONException {
 
		JSONObject jsonObject = new JSONObject();
		float celsius;
		celsius = (f - 32) * 5 / 9;
		jsonObject.put("F Value", f);
		jsonObject.put("C Value", celsius);
 
		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}
}