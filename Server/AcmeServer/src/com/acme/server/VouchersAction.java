package com.acme.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acme.server.database.DBConnection;
import com.acme.server.validation.VouchersRequest;

/* ------------------------------------- *
 *			 HISTORY ACTION
 * ------------------------------------- */

// curl -d '{"UUID": UUID}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/vouchers


@Path("/vouchers")
public class VouchersAction {

	private JSONObject res;
	private VouchersRequest req;
	private Connection connection = null;

	public VouchersAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	private void getVouchersInformation() throws SQLException {
		res = new JSONObject();
		
		final String stmt = "SELECT VOUCHERS_AVLB FROM USERS WHERE UUID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setString(1, req.getUUID().toString());
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			res = null;
			return;
		}
		res.put("vouchers", rs.getInt("VOUCHERS_AVLB"));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response historyAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		req = new VouchersRequest(data);

		try {
			if(!req.isValid()) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
			}
			
			getVouchersInformation();
			
			if(res == null) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(res.toString()).build();
			}
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			
		} catch (SQLException e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(e.getMessage()).build();
		}
	}
}
