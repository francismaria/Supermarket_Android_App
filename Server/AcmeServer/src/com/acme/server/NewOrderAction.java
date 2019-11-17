package com.acme.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import com.sun.jersey.core.util.Base64;

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
	
	private String getUserRawPublicKey(int userID) throws SQLException {
		final String stmt = "SELECT PUBLIC_KEY FROM USERS WHERE UUID = ?";
		
		PreparedStatement pStmt = connection.prepareStatement(stmt);
		pStmt.setInt(1, userID);
		
		ResultSet rs = pStmt.executeQuery();
		
		if(!rs.next()) {
			return null;
		}
		
		return rs.getString("PUBLIC_KEY");
	}
	
	private PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		// can't be hardcoded
		String stringPK = getUserRawPublicKey(3);		// get pk from the database
		
		byte[] publicKeyBytes = Base64.decode(stringPK);
		
		X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey pk = kf.generatePublic(X509publicKey);
        return pk;
	}
	
	/**
	 * Verifies the signature of the message.
	 * 
	 * @param mess
	 * @param sign
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SQLException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	private boolean verifySignature(byte[] mess, byte[] sign) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, InvalidKeyException, SignatureException {
		PublicKey pk = getUserPublicKey();
		
		Signature sg = Signature.getInstance("SHA256WithRSA");
		
		sg.initVerify(pk);
		sg.update(mess);
		
		boolean verified = sg.verify(sign);
		
	      
	    if(verified) {
	       System.out.println("Signature verified");   
	    } else {
           System.out.println("Signature failed");
        }
		
	    return verified;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newOrderAction(String data) {
		if(connection == null) 
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		
		try {
			
			// get user's public key to decrypt the digital signature
			// verify signature

			
			byte[] msg = Base64.decode(data);
			
			ByteBuffer tag = ByteBuffer.wrap(msg);
			int sign_size = 512/8;
		    int mess_size = msg.length - sign_size;
            
		    byte[] mess = new byte[mess_size];
		    byte[] sign = new byte[sign_size];
		    tag.get(mess, 0, mess_size);
		    tag.get(sign, 0, sign_size);
		    
		    boolean verified = verifySignature(mess, sign);
		     
		    JSONObject res = new JSONObject();
		    res.put("msg", verified);	

			
			/*
			 * 
			 *            
			 *             
		
      
      
			ByteBuffer tag = ByteBuffer.wrap(clearTag);
            // Tag ID
            int tId = tag.getInt();
            // UUID
            UUID id = new UUID(tag.getLong(), tag.getLong());
            // Price
            int euros = tag.getInt();
            int cents = tag.getInt();
            // Product Name
            byte[] bName = new byte[tag.get()];
            tag.get(bName);
            String name = new String(bName, StandardCharsets.ISO_8859_1);
            String priceStr = euros + "." + cents;



			Signature sg = Signature.getInstance("SHA256WithRSA");
			// get client public key
			sg.initVerify();
			sg.verify(tag);*/
			/*
		      //Initializing the signature
		      sign.initVerify(pair.getPublic());
		      sign.update(bytes);
		      
		      //Verifying the signature
		      boolean bool = sign.verify(signature);
		      
		      if(bool) {
		         System.out.println("Signature verified");   
		      } else {
		         System.out.println("Signature failed");
		      }*/
			
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
		
				
	}
/*
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

			byte[] arr = {85, 83, 44, 13, -41, 11, 13, -80, -66, 55, 127, -59, 123, -113, 88, -100, 93, -118, 65, -89, -52, -72, -94, -44, -108, 90, -2, 54, 4, 86, 2, 81, -33, -87, -52, -62, -80, 26, -89, 50, 106, -119, -108, -77, -35, -33, 27, 81, -121, 91, -55, 126, 60, -33, -123, -94, -92, -16, 89, -107, 54, 15, 42, 87, 114, -38, 28, 62, 94, 108, 27, 120, 36, -91, 39, -80, -15, 4, -105, -54, -94, 89, 112, 90, 23, 13, -16, -126, 86, -9, -22, -25, -78, -55, -81, -110, -92, 108, -50, 3, 6, 87, 31, 14, 20, 41, 115, 17, -59, -115, 43, 9, -14, 59, 88, 118, 33, -75, 52, 35, -93, 49, -112, 27, -112, 36, 92, 64, 23, -52, 15, -41, -41, 79, 127, 65, 13, -42, -20, 94, -122, -32, 114, 109, 123, -36, 2, -104, -109, -19, -115, 114, 10, 48, -116, 19, 114, 109, -40, 16, -121, 87, 84, 56, -28, -41, 67, -28, -66, 102, -14, -108, 42, 120, -8, 113, 73, -117, -115, 1, -22, -8, 83, -38, 13, 100, -19, 108, 32, 53, 76, -70, -68, 108, -108, -93, -98, 48, -4, -40, -15, 43, 13, 106, 28, 113, -26, 26, 89, -110, -100, 1, -62, -94, 54, 10, -79, 38, 98, 67, 99, 104, -32, 34, -4, 83, 60, -51, 14, -30, 0, -117, 105, -32, -96, -21, 18, -67, 74, 72, 13, 116, -39, 19, 89, 93, 21, -123, -11, -106, -56, 126, 124, 7, -66, 9};

			String res = Cryptography.decrypt(arr, KeyInstance.getPrivateKey());
			/*byte[] arr = {65, 99, 109, 101, 25, 64, -47, 22, 44, -30, 72, 17, -67, 61, -123, -7, 111, 16, -36, -94, 0, 0, 0, 17, 0, 0, 0, 99, 27, 78, 105, 118, 101, 97, 32, 83, 117, 110, 107, 105, 115, 115, 101, 100, 32, 66, 111, 100, 121, 32, 76, 111, 116, 105, 111, 110};

			
			String res = Arrays.toString(Cryptography.encrypt(arr, KeyInstance.getPrivateKey()));
					
			closeConnection();
			return Response.status(HTTPCodes.SUCCESS_CODE).entity(res).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(HTTPCodes.INTERNAL_SERVER_ERROR_CODE).entity(null).build();
		}
	}*/
}
