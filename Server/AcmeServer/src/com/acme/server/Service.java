package com.acme.server;

import java.lang.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.validation.Constraint;
import javax.validation.Payload;
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

import com.acme.server.database.DBConnection;



@Path("/public")
public class Service {
	
	private Connection connection = null;

	public Service() {
		connection = (new DBConnection()).getConnection();
	}
	
	
	// TO BE DELETED
	
	@GET
	@Produces("application/json")
	public Response convertFtoC() throws JSONException {
		
		if(connection == null) {
			String result = "NOT CONNECTED TO DB";
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(result).build();
		}
 
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user", "example");
		jsonObject.put("password", "123456");
 
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(jsonObject.toString()).build();
	}
	
	@POST
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response example(String json) throws JSONException {
		
		JSONObject obj = new JSONObject(json);
		
		
		return Response.status(HTTPCodes.SUCCESS_CODE).entity(obj.toString()).build();
	}

}