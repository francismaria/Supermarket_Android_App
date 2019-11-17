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

import com.acme.server.database.DBConnection;
import com.acme.server.validation.ContactRequest;

/* ------------------------------------- *
 *			 CONTACT ACTION
 * ------------------------------------- */

@Path("/contact")
public class ContactAction {

	private Connection connection = null;

	public ContactAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	private String getUserEmail(ContactRequest req) throws Exception {
		final String stmt = "SELECT EMAIL FROM USERS WHERE UUID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setString(1, req.getUUID().toString());
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			throw new Exception("UUID does not exist");
		}
		return rs.getString("EMAIL");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response contactAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		ContactRequest req = new ContactRequest(data);
		
		try {
			if(!req.isValid()) {
				closeConnection();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			}
			
			try {
				String userEmail = getUserEmail(req);
			} catch(SQLException e) {
				closeConnection();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			} catch(Exception e) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(null).build();
			}
				
			// send email to user
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		} catch(Exception e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
