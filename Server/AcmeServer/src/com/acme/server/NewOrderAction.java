package com.acme.server;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.acme.server.database.DBConnection;
import com.acme.server.security.Cryptography;
import com.acme.server.security.KeyInstance;

/* ------------------------------------- *
 *			 NEW ORDER ACTION
 * ------------------------------------- */

// curl -d '{"UUID":"1"}' -H "Content-Type: application/json" -X POST http://localhost:8080/AcmeServer/api/new-order

@Path("/new-order")
public class NewOrderAction {

	private Connection connection = null;

	public NewOrderAction() {
		connection = (new DBConnection()).getConnection();
	}
	
	private void closeConnection() throws SQLException {
		connection.close();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newOrderAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		JSONObject req = new JSONObject(data);
		
		//String s = req.getString("msg");
	
		try {	
			//byte[] arr = {32, 106, -46, -46, -88, 14, 49, -20, 21, 105, 51, -105, 49, 64, 21, -60, 73, -76, 119, -60, -55, -114, -13, 66, -30, 124, 34, 38, 106, 116, 122, 111, 38, -43, 62, 77, 5, 21, 73, -69, 126, -14, 55, -88, -27, 101, -44, 68, -20, -36, 7, -75, 2, -68, 105, 18, -21, -88, -20, 114, -76, -125, -123, 32, -43, 98, -29, 12, -107, -86, 31, -71, -92, -119, 110, -106, 93, -40, 9, 80, -71, 80, 95, -102, -7, -72, 121, 65, -79, -40, 34, 119, 110, -91, -19, 127, 85, -87, -122, -27, -8, -58, 74, 57, -116, 12, 39, -127, -69, -123, 4, 90, 114, 106, 71, 48, 73, 11, -119, 66, 112, -74, 29, -65, -16, -28, -108, 12, -3, -18, 63, 16, -36, -85, 20, 20, 40, 18, 24, 5, 83, -54, -99, 4, -13, -123, 47, 56, 42, -101, -94, 74, 11, -19, -54, -69, 70, 15, 19, -52, -4, -94, -80, -49, 78, 117, -20, -55, -90, -70, -67, -53, -42, 31, 48, 2, 121, -48, -110, -87, 93, -17, 90, -8, 53, -85, 16, 2, 74, -36, 110, -13, -126, 72, 76, -39, 27, 56, 120, -113, 23, 87, 67, -99, -35, -74, 88, -118, -46, 2, -63, 64, -96, -25, -111, -44, -3, -90, 13, -123, -36, 44, -94, 27, 2, 19, -122, -94, -120, 110, -91, -31, -99, 15, -115, -8, -43, -26, -2, -108, 30, 73, 71, -123, 68, 99, -30, -8, -105, 107, -5, 103, -43, -69, -86, -55};

			byte[] arr = {2, 44, 62, -105, 53, -15, -116, -114, 77, -16, -30, 117, 81, 108, -78, 16, 70, 62, 56, 124, 24, -99, -60, 79, 87, -124, 57, -62, -107, -90, -127, 3, 32, -89, 70, 41, -9, -78, -13, 33, -117, 59, 79, -27, -77, -71, -122, -54, -109, -111, 57, -3, 17, 45, 58, 5, -43, -97, 16, 12, -32, -83, 58, -120, 18, 81, -123, 79, -41, -25, -23, -39, 90, 65, -11, -4, -85, -112, 95, 122, -125, 68, 52, -95, 15, 53, 34, -9, -61, -4, -9, -25, -81, 28, -94, 56, 51, -115, -91, 93, -48, -53, -22, 2, 70, 97, -25, -127, 99, -68, -94, -3, -87, -74, -34, 65, 37, -96, 19, -117, -3, 91, -40, 12, 13, 44, 71, -40, -13, 86, 95, -20, 1, 96, 119, -103, 21, 123, 0, -77, 57, -5, -77, 61, 95, -38, 81, 120, -67, 92, 62, 44, 6, 116, 83, 105, 10, 39, -70, -48, 82, -69, -21, 0, -61, 49, -90, -45, -2, 36, -103, 68, 106, 116, 38, -98, 20, -62, 40, 124, -8, 37, 47, -73, 112, -15, -53, 108, 26, -43, -72, -71, -11, -74, -31, -111, 67, 20, 32, -122, 75, -82, 105, -75, 71, 58, 115, -16, 67, -33, -8, 46, -3, 40, 63, 24, 66, 83, 107, -24, -56, 127, 115, 69, -30, 64, -68, 82, -1, 46, 25, 0, 81, -45, 82, -47, 72, -20, 73, 126, 99, -38, -34, -42, 78, -113, -100, 75, -105, -93, -48, 54, -89, -128, 0, 127};
			
			String res = Cryptography.decrypt(arr, KeyInstance.getPrivateKey());
			
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
