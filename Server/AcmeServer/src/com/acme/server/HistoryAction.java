package com.acme.server;

import java.sql.Connection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acme.server.database.DBConnection;
import com.acme.server.validation.HistoryRequest;

/* ------------------------------------- *
 *			 HISTORY ACTION
 * ------------------------------------- */

@Path("/history")
public class HistoryAction {

	private Connection connection = null;

	public HistoryAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	//private boolean publicKeyMatches() {}
	
	// String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date()); -> gets current date in the specific format
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response historyAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		HistoryRequest req = new HistoryRequest(data);

		if(!req.isValid()) {
			return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
		}
		
		
		
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		
		/*
		if(!publicKeyMatches()) {
			
		}*/
	}
}