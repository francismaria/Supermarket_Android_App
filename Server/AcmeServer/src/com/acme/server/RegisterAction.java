package com.acme.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

/* ------------------------------------- *
 *			   LOGIN ACTION
 * ------------------------------------- */

@Path("/register")
public class RegisterAction {

	private Connection connection = null;

	public RegisterAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	/* ------------------------------------- *
	 *			  REGISTER ACTION
	 * ------------------------------------- */
	
	private boolean uniqueUsername(String username) {
		
		if(connection == null) return false;
		
		final String USERNAME_EXISTS_QUERY = "SELECT USERNAME FROM USERS WHERE USERNAME = ?";
		
		try {
			PreparedStatement pStmt = connection.prepareStatement(USERNAME_EXISTS_QUERY);
			pStmt.setString(1, username);
			
			ResultSet rs = pStmt.executeQuery();
			
			if(!rs.next()) 
				return true;
			return false;
			
		} catch (SQLException e) {
			System.out.println("Error: " + e .toString());
			return false;
		}
	}

	private int getNewUserUUID() {
		if(connection == null)
			return -1;			// throw NonExisitingConnection
		
		int nextUUID = -1;
		final String MAX_ID_QUERY = "SELECT MAX(UUID) FROM USERS";
		
		try {
			PreparedStatement pStmt = connection.prepareStatement(MAX_ID_QUERY);
			ResultSet rs = pStmt.executeQuery();

			nextUUID = rs.getInt("MAX(UUID)")+1;
		} catch (SQLException e) {
			System.out.println("Error: " + e .toString());
			return -1;
		}
		
		return nextUUID;
	}
	
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response registerAction(String data) throws JSONException {
		
		if(connection == null) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		JSONObject objData = new JSONObject(data);
		
		if(!uniqueUsername((String) objData.get("username"))) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		int newUUID = getNewUserUUID();
		
		
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		
		/*
		JSONObject objData = new JSONObject(data);
		final String stmt = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";
		
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
		*/
		//return Response.status(Service.SUCCESS_CODE).entity(null).build();
	}
}
