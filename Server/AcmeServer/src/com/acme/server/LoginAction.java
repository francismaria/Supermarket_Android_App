package com.acme.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.json.JSONException;
import org.json.JSONObject;

/* ------------------------------------- *
 *			   LOGIN ACTION
 * ------------------------------------- */

@Path("/login")
public class LoginAction {

	private Connection connection = null;

	public LoginAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	/**
	 * Checks if the JSON body comes according to the expected JSON schema.
	 * @return
	 */
	private boolean schemaMatches(JSONObject body) {
		return true;
	}

	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response loginAction(@Valid String data) throws JSONException {
		
		if(connection == null) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		JSONObject objData = new JSONObject(data);
		/*
		if(!schemaMatches(objData)) {
			return Response.status(HTTPCodes.BAD_REQUEST).entity(null).build();
		}
		*/
		final String stmt = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";
		
		try {
			PreparedStatement pStmt = connection.prepareStatement(stmt);
			pStmt.setString(1, objData.getString("username"));
			
			ResultSet rs = pStmt.executeQuery();
			
			if(!rs.next()) {
				return Response.status(HTTPCodes.UNAUTHORIZED_CODE).entity(null).build();
			}
			
			// is this correct?? ---> TEST THIS
			if(!rs.getString("password").equals(objData.getString("password"))) {
				return Response.status(HTTPCodes.UNAUTHORIZED_CODE).entity(null).build();
			}
			
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
			
		} catch (SQLException e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
