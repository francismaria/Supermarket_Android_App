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

import org.json.JSONObject;

import com.acme.server.database.DBConnection;
import com.acme.server.validation.VerifyUsernameRequest;

// curl -d '{"username": "smendes"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/verify-username

/* ------------------------------------- *
 *	     VERIFY USERNAME ACTION
 * ------------------------------------- */

@Path("/verify-username")
public class VerifyUsernameAction {
	private Connection connection = null;

	public VerifyUsernameAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	private boolean isUsernameUnique(VerifyUsernameRequest req) throws Exception {
		final String stmt = "SELECT UUID FROM USERS WHERE USERNAME = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setString(1, req.getUsername());
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			return true;
		}
		return false;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response contactAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		VerifyUsernameRequest req = new VerifyUsernameRequest(data);
		JSONObject res;
		
		try {
			if(!req.isValid()) {
				closeConnection();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			}
			
			res = new JSONObject();
			
			if(isUsernameUnique(req)){
				res.put("res", true);
			} else {
				res.put("res", false);
			}
				
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
		} catch(Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}

}

