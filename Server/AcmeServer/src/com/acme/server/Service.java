package com.acme.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/api")
public class Service {
	
	private Connection connection = null;
	
	private static final int SUCCESS_CODE = 200;
	private static final int UNAUTHORIZED_CODE = 401;
	private static final int FORBIDDEN_CODE = 403;
	private static final int INTERNAL_SERVER_ERROR_CODE = 500;

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
			return Response.status(Service.INTERNAL_SERVER_ERROR_CODE).entity(result).build();
		}
 
		JSONObject jsonObject = new JSONObject();
		Double fahrenheit = 98.24;
		Double celsius;
		celsius = (fahrenheit - 32) * 5 / 9;
		jsonObject.put("user", "example");
		jsonObject.put("password", "123456");
 
		return Response.status(Service.SUCCESS_CODE).entity(jsonObject.toString()).build();
	}
 
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response loginAction(String data) throws JSONException {
		JSONObject objData = new JSONObject(data);
		
		final String stmt = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";
		
		if(connection == null) {
			return Response.status(Service.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		try {
			PreparedStatement pStmt = connection.prepareStatement(stmt);
			pStmt.setString(1, objData.getString("username"));
			
			ResultSet rs = pStmt.executeQuery();
			
			if(!rs.next()) {
				return Response.status(Service.UNAUTHORIZED_CODE).entity(null).build();
			}
			
		} catch (SQLException e) {
			return Response.status(Service.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		return Response.status(Service.SUCCESS_CODE).entity(null).build();
	}
}