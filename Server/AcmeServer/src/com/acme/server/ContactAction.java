package com.acme.server;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	//private boolean publicKeyMatches() {}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response contactAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		
		/*ContactRequest req = new ContactRequest(data);

		if(!req.isValid()) {
			return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
		}
		
		
		
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(null).build();
		*/
		/*
		if(!publicKeyMatches()) {
			
		}*/
	}
}
