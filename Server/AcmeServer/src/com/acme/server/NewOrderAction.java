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

			byte[] arr = {122, 97, -128, 78, -71, 52, 51, 29, 27, 84, -80, -10, -86, 24, 52, -86, 62, -96, 80, 24, -67, 87, -80, -25, -66, 28, 50, -55, -118, 114, -39, -114, 50, -29, 25, 6, -80, -41, -82, 61, 101, 50, -100, 63, 3, 80, -103, 12, 83, -31, -18, -60, 103, -83, -66, 42, -42, -71, 110, 100, -15, 84, 22, -97, -114, -70, 98, 87, -103, 17, -51, 121, 4, -12, 41, -42, 104, -40, -3, 50, 31, -59, 21, 18, -23, -25, -124, -83, -33, -36, -85, -74, -34, 107, -52, -114, -65, 58, -106, -24, 117, 103, 81, 99, -91, 56, -17, 93, 48, 47, -49, 35, -102, 20, 65, 32, 19, 11, 80, 31, 114, -98, -75, 21, -114, 35, 24, -80, -102, -107, -128, 81, -22, -40, -124, 53, 33, -65, 101, 125, 36, -124, 91, 40, -57, 85, -115, -73, 106, 109, -119, -62, 40, -91, 22, -63, 61, 20, 106, -116, 3, 71, -26, -38, 11, 110, 114, 122, 28, -19, 59, 107, -64, -25, -61, -68, 59, 123, 96, 59, 114, -8, 7, 49, 74, 85, -79, 101, -53, 69, 68, 96, -123, 122, -106, -51, 45, 114, 85, -75, 96, 81, -18, -23, -71, 51, 104, 77, 26, 39, 68, 106, -111, -11, -124, -94, 83, -29, 78, -7, 35, -38, -47, -5, -30, 102, -99, -110, -105, 114, 66, 29, 62, -12, 54, -84, -33, 32, -9, -56, 46, -93, -16, -80, -86, -12, 15, -40, 5, 106, -9, -107, -65, -95, -1, 49};

			
			String res = Cryptography.decrypt(arr, KeyInstance.getPrivateKey());
			
			
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}
}
