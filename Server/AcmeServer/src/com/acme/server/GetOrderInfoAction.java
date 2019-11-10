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
import com.acme.server.validation.GetOrderInfoRequest;
import com.acme.server.validation.HistoryRequest;

/* ------------------------------------- *
 *			 HISTORY ACTION
 * ------------------------------------- */

// curl -d '{"UUID":"1", "orderID": "1"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/order

@Path("/order")
public class GetOrderInfoAction {

	private JSONObject res;
	private GetOrderInfoRequest req;
	private Connection connection = null;

	public GetOrderInfoAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}
	
	//private boolean publicKeyMatches() {}
	
	// String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date()); -> gets current date in the specific format
	
	// "SELECT DATE, VOUCHERS, TOTAL_COST, PRODUCT_ID, PRICE, NAME, QUANTITY FROM TRANSACTIONS INNER JOIN HISTORY ON TRANSACTIONS.ID = HISTORY.TRANSACTION_ID INNER JOIN PRODUCTS ON HISTORY.PRODUCT_ID = PRODUCTS.UUID WHERE TRANSACTIONS.USER_ID = ? AND TRANSACTIONS.ID = ?;";
	
	private void getGeneralInformation() throws SQLException {
		final String stmt = "SELECT DATE, VOUCHERS, TOTAL_COST FROM TRANSACTIONS WHERE TRANSACTIONS.USER_ID = ? AND TRANSACTIONS.ID = ?;";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setInt(1, req.getUUID());
		pStmt.setInt(2, req.getOrderID());
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			res = null;
			return;
		}
		
		res.put("orderID", req.getOrderID());
		res.put("vouchers", rs.getInt("VOUCHERS"));
		res.put("date", rs.getString("DATE"));
		res.put("total_cost", rs.getString("TOTAL_COST"));
	}
	
	private void getOrderInformation() throws SQLException {
		res = new JSONObject();
		
		getGeneralInformation();
		
		final String stmt = "SELECT PRODUCT_ID, NAME, PRICE, QUANTITY FROM TRANSACTIONS INNER JOIN HISTORY ON TRANSACTIONS.ID = HISTORY.TRANSACTION_ID INNER JOIN PRODUCTS ON HISTORY.PRODUCT_ID = PRODUCTS.UUID WHERE TRANSACTIONS.USER_ID = ? AND TRANSACTIONS.ID = ?;";
		
				
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setInt(1, req.getUUID());
		pStmt.setInt(2, req.getOrderID());
		
		ResultSet rs = pStmt.executeQuery();
		JSONArray itemsArr = new JSONArray();
		
		while(rs.next()) {
			JSONObject item = new JSONObject();
		
			item.put("productID", rs.getInt("PRODUCT_ID"));
			item.put("productName", rs.getString("NAME"));
			item.put("productPrice", rs.getDouble("PRICE"));
			item.put("productQty", rs.getInt("QUANTITY"));

			itemsArr.put(item);
		}
		res.put("products", itemsArr);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderInfoAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		req = new GetOrderInfoRequest(data);

		try {
			if(!req.isValid()) {
				closeConnection();
				return Response.status(HTTPCodes.BAD_REQUEST).entity(req.getErrorMsg()).build();
			}
			
			getOrderInformation();
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
			
		} catch (SQLException e) {
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(e.getMessage()).build();
		}
		/*
		if(!publicKeyMatches()) {
			
		}*/
	}
}
