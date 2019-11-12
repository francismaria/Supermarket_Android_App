package com.acme.server.validation;

import org.json.JSONObject;

public class RegisterRequest extends Request {

	// Request parameters
	private String name;
	private String username;
	private String password;
	private String email;
	private String cardNr;
	private String cardExpDate;
	private String cardCCV; 
	private String publicKey;
	
	private JSONObject obj;
	
	
	// Name
	private void setName() throws Exception {
		if(!obj.has("name")) {
			throw new Exception("name was not specified.");
		}
		this.name = obj.getString("name");
	}
	
	// Username
	private void setUsername() throws Exception {
		if(!obj.has("username")) {
			throw new Exception("username was not specified.");
		}
		this.username = obj.getString("username");
	}
	
	// Password
	private void setPassword() throws Exception {
		if(!obj.has("password")) {
			throw new Exception("password was not specified.");
		}
		this.password = obj.getString("password");
	}

	// Email
	private void setEmail() throws Exception {
		if(!obj.has("email")) {
			throw new Exception("email was not specified.");
		}
		this.email = obj.getString("email");
	}
	
	// CardNr
	private void setCardNr() throws Exception {
		if(!obj.has("cardNr")) {
			throw new Exception("card nr was not specified.");
		}
		this.cardNr = obj.getString("cardNr");
	}
	
	// Card Exp Date
	private void setCardExpDate() throws Exception {
		if(!obj.has("cardExpDate")) {
			throw new Exception("card Exp Date was not specified.");
		}
		this.cardExpDate = obj.getString("cardExpDate");
	}
	
	// Card CCV
	private void setCardCCV() throws Exception {
		if(!obj.has("cardCCV")) {
			throw new Exception("card CCV was not specified.");
		}
		this.cardCCV = obj.getString("cardCCV");
	}
	
	// Public Key
	private void setPublicKey() throws Exception {
		if(!obj.has("publicKey")) {
			throw new Exception("public key was not specified.");
		}
		this.publicKey = obj.getString("publicKey");
	}
	
	private void setKeys() {
		try {
			setName();
			setUsername();
			setPassword();
			setEmail();
			setCardNr();
			setCardExpDate();
			setCardCCV();
			setPublicKey();
		} catch(Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public RegisterRequest(String req) {
		setValid(true);
		setErrorMsg("");
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public String getName() {
		return name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCardNr() {
		return cardNr;
	}
	
	public String getCardExpDate() {
		return cardExpDate;
	}
	
	public String getCardCCV() {
		return cardCCV;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
}
