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

import com.acme.server.validation.ProfileRequest;

/* ------------------------------------- *
 *			 PROFILE ACTION
 * ------------------------------------- */

// curl -d '{"UUID":"1" ,"publicKey":"asdaksdlakdlak1231ewdda"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/profile

@Path("/profile")
public class ProfileAction {
	
	private JSONObject res;
	private Connection connection = null;

	public ProfileAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	private void getCreditCardInfo(int cardID) throws SQLException {
		final String stmt = "SELECT NUMBER, EXP_DATE, CCV FROM CARDS WHERE ID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setInt(1, cardID);
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			res = null;
			return;
		}
		
		res.put("cardNr", rs.getString("NUMBER"));
		res.put("cardExpDate", rs.getString("EXP_DATE"));
		res.put("cardCCV", rs.getString("CCV"));
	}
	
	private void getPersonalInfo(ProfileRequest req) throws SQLException {
		final String stmt = "SELECT NAME, USERNAME, EMAIL, CARD_ID FROM USERS WHERE UUID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setInt(1, req.getUUID());
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			res = null;
			return;
		}
		
		res.put("name", rs.getString("NAME"));
		res.put("username", rs.getString("USERNAME"));
		res.put("email", rs.getString("EMAIL"));
		
		getCreditCardInfo(rs.getInt("CARD_ID"));
	}
	
	private void constructResponse(ProfileRequest req) throws SQLException {
		res = new JSONObject();
		getPersonalInfo(req);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response profileAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		// request validation
		ProfileRequest req = new ProfileRequest(data);
		
		try {
			if(!req.isValid()) {
				res = new JSONObject();
				res.put("msg", req.getErrorMsg());
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
			}
			
			// response creation
			constructResponse(req);
			
			// response validation
			if(res == null) {
				closeConnection();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			}
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
		} catch(SQLException e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
