package com.acme.server;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
import com.acme.server.validation.RegisterRequest;

/* ------------------------------------- *
 *			 REGISTER ACTION
 * ------------------------------------- */

/*
 * CURL EXAMPLE - testing
 * 
 * curl -d '{"username":"username"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/register/validate-username
 * 
 * curl -d '{"username":"username", "password":"francis", "name":"francisco", "email":"example", "cardNr":"1234", "cardExpDate":"12/12", "cardCCV":"123", "publicKey":"asdaksdlakdlak1231ewdda"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/register
 */

@Path("/register")
public class RegisterAction {

	private JSONObject res;
	private Connection connection = null;

	public RegisterAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
			connection.close();
	}
	
	/**
	 * Checks if the username of the new user to be registered is
	 * or not unique.
	 * 
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	private boolean uniqueUsername(String username) throws SQLException {
		
		if(connection == null) return false;
		
		final String USERNAME_EXISTS_QUERY = "SELECT USERNAME FROM USERS WHERE USERNAME = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(USERNAME_EXISTS_QUERY);
		pStmt.setString(1, username);
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) 
			return true;
		return false;
	}
	/*
	@Path("/validate-username")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response isUsernameUnique(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		try {
			JSONObject req = new JSONObject(data);
			JSONObject res = new JSONObject();
			
			String username = req.getString("username");
			
			if(!req.has("username")) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(null).build();
			}
			
			if(!uniqueUsername(username)) {
				closeConnection();
				res.put("unique", false);
				return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			}
			
			res.put("unique", true);
			
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
		} catch(Exception e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
*/
	/**
	 * Gets the new UUID of the new user.
	 * 
	 * It gets from the database the the maximum UUID and increments it by one.
	 * 
	 * @return UUID
	 */
	private UUID getNewUserUUID() {
		return UUID.randomUUID();
	}
	
	private UUID registerNewUser(RegisterRequest req) {
	   String INSERT_NEW_USER_QUERY = "INSERT INTO USERS(UUID, NAME, EMAIL, USERNAME, PASSWORD, CARD_ID, PUBLIC_KEY) VALUES(?,?,?,?,?,?,?)";
       UUID newUUID =  getNewUserUUID();
       
        try {
            PreparedStatement pstmt = connection.prepareStatement(INSERT_NEW_USER_QUERY);
            pstmt.setString(1, newUUID.toString());
            pstmt.setString(2, req.getName());
            pstmt.setString(3, req.getEmail());
            pstmt.setString(4, req.getUsername());
            pstmt.setString(5, req.getPassword());
            pstmt.setInt(6, 1);			
            pstmt.setString(7, req.getPublicKey());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
            return null;
        }
        return newUUID;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response registerAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		RegisterRequest req;
		res = new JSONObject();
		
		req = new RegisterRequest(data);
		
		try {
			if(!req.isValid()) {
				closeConnection();
				res.put("msg", req.getErrorMsg());
				return Response.status(HTTPCodes.BAD_REQUEST).entity(res.toString()).build();
			}
		
			if(!uniqueUsername(req.getUsername())) {
				closeConnection();
				res.put("msg", "username must be unique");
				return Response.status(HTTPCodes.BAD_REQUEST).entity(res.toString()).build();
			}
			
			UUID newUUID = registerNewUser(req);
					
			res.put("username", req.getUsername());
			res.put("UUID", newUUID.toString());
			
			try {
				res.put("acmePK", KeyInstance.getPublicKey());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
			}
			
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			
		} catch (SQLException | JSONException | FileNotFoundException | CertificateException e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
