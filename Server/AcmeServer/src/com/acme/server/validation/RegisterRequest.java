package com.acme.server.validation;

import org.json.JSONObject;

public class RegisterRequest implements RequestValidation {

	private String name;
	private String username;
	private String password;
	private String email;
	private String cardNr;
	private String cardExpDate;
	private String cardCCV; 
	private String publicKey;
	
	private boolean valid;
	
	private JSONObject obj;
	
	private String errorMsg;
	
	// Name
	private void setName() throws Exception {
		if(!obj.has("name")) {
			this.valid = false;
			throw new Exception("name was not specified.");
		}
		this.name = obj.getString("name");
	}
	
	// Username
	private void setUsername() throws Exception {
		if(!obj.has("username")) {
			this.valid = false;
			throw new Exception("username was not specified.");
		}
		this.username = obj.getString("username");
	}
	
	// Password
	private void setPassword() throws Exception {
		if(!obj.has("password")) {
			this.valid = false;
			throw new Exception("password was not specified.");
		}
		this.password = obj.getString("password");
	}

	// Email
	private void setEmail() throws Exception {
		if(!obj.has("email")) {
			this.valid = false;
			throw new Exception("email was not specified.");
		}
		this.email = obj.getString("email");
	}
	
	// CardNr
	private void setCardNr() throws Exception {
		if(!obj.has("cardNr")) {
			this.valid = false;
			throw new Exception("card nr was not specified.");
		}
		this.cardNr = obj.getString("cardNr");
	}
	
	// Card Exp Date
	private void setCardExpDate() throws Exception {
		if(!obj.has("cardExpDate")) {
			this.valid = false;
			throw new Exception("username was not specified.");
		}
		this.cardExpDate = obj.getString("cardExpDate");
	}
	
	// Card CCV
	private void setCardCCV() throws Exception {
		if(!obj.has("cardCCV")) {
			this.valid = false;
			throw new Exception("username was not specified.");
		}
		this.cardCCV = obj.getString("cardCCV");
	}
	
	// Public Key
	private void setPublicKey() throws Exception {
		if(!obj.has("publicKey")) {
			this.valid = false;
			
			throw new Exception("username was not specified.");
		}
		this.publicKey = obj.getString("publicKey");
	}
	
	
	private void setKeys() throws Exception {
		setName();
		setUsername();
		setPassword();
		setEmail();
		setCardNr();
		setCardExpDate();
		setCardCCV();
		setPublicKey();
	}
	
	public RegisterRequest(String req) throws Exception {
		this.valid = true;
		this.obj = new JSONObject(req);
		this.errorMsg = "";
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
	
	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

}
