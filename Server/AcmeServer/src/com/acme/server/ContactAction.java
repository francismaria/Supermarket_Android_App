package com.acme.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

// curl -d '{"UUID": "d888babc-c2ba-45c9-b6b4-5e0661f06ffe", "msg": "Very good application. I am very happy"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/contact
	
@Path("/contact")
public class ContactAction {

	private Connection connection = null;
	private static final String CONTACT_FILENAME = "///Users/francisco/Documents/FEUP/Projetos/CMOV/Server/contact_requests.txt";

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
	
	
	/**
	 * Gets current date in the dd-mm-yyyy format.
	 * 
	 * @return String
	 */
	private String getCurrentDate() {
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}
	
	private void writeEntryToFile(String email, ContactRequest req) throws IOException {
		Writer output;
		output = new BufferedWriter(new FileWriter(CONTACT_FILENAME, true));
		output.append("\n["+getCurrentDate()+ " - " + email + "] : " + req.getMsg());
		output.close();
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
				
				writeEntryToFile(userEmail, req);
				
				
			} catch(SQLException e) {
				e.printStackTrace();
				closeConnection();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			} catch(Exception e) {
				e.printStackTrace();
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(null).build();
			}
				
			// send email to user
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		} catch(Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
