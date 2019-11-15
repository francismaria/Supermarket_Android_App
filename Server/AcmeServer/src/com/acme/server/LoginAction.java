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

import com.acme.server.database.DBConnection;
import com.acme.server.security.KeyInstance;

/* ------------------------------------- *
 *			   LOGIN ACTION
 * ------------------------------------- */

// curl -d '{"username":"francis", "password":"password"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/login
	
@Path("/login")
public class LoginAction {

	private JSONObject res;
	private Connection connection = null;

	public LoginAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	private void getUserInformation(String username) throws SQLException {
		final String stmt = "SELECT USERNAME, UUID FROM USERS WHERE USERNAME = ?;";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setString(1, username);
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			res = null;
			return;
		}
		
		res.put("username", rs.getString("USERNAME"));
		res.put("UUID", rs.getInt("UUID"));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response loginAction(String data) throws JSONException {
		
		if(connection == null) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
		JSONObject objData = new JSONObject(data);

		final String stmt = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";
		
		try {
			PreparedStatement pStmt = connection.prepareStatement(stmt);
			pStmt.setString(1, objData.getString("username"));
			
			ResultSet rs = pStmt.executeQuery();
			
			if(!rs.next()) {
				closeConnection();
				return Response.status(HTTPCodes.UNAUTHORIZED_CODE).entity(null).build();
			}
			
			if(!rs.getString("password").equals(objData.getString("password"))) {
				closeConnection();
				return Response.status(HTTPCodes.UNAUTHORIZED_CODE).entity(null).build();
			}
			
			res = new JSONObject();
			
			getUserInformation(objData.getString("username"));
			res.put("acmePK", KeyInstance.getPublicKey());
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
