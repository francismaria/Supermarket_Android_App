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
import com.acme.server.validation.HistoryRequest;

/* ------------------------------------- *
 *			 HISTORY ACTION
 * ------------------------------------- */

// curl -d '{"UUID":"1"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/history


@Path("/history")
public class HistoryAction {

	private JSONObject res;
	private HistoryRequest req;
	private Connection connection = null;

	public HistoryAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	//private boolean publicKeyMatches() {}
	
	// String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date()); -> gets current date in the specific format
	
	private void getHistoryItemsInformation() throws SQLException {
		res = new JSONObject();
		
		final String stmt = "SELECT ID, DATE, TOTAL_COST FROM TRANSACTIONS WHERE USER_ID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setString(1, req.getUUID().toString());
		
		ResultSet rs = pStmt.executeQuery();
		JSONArray itemsArr = new JSONArray();
		
		while(rs.next()) {
			JSONObject item = new JSONObject();
			
			item.put("id", rs.getInt("ID"));
			item.put("date", rs.getString("DATE"));
			item.put("total_cost", rs.getDouble("TOTAL_COST"));
			
			itemsArr.put(item);
		}
		
		res.put("items", itemsArr);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response historyAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		req = new HistoryRequest(data);

		try {
			if(!req.isValid()) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
			}
			
			getHistoryItemsInformation();
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			
		} catch (SQLException e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(e.getMessage()).build();
		}
	}
}