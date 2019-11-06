package com.acme.server.validation;

import org.json.JSONObject;

public class ProfileRequest implements RequestValidation {
	private int UUID;
	private String publicKey;
	
	private JSONObject obj;
	
	// Validates the request
	private boolean valid;
	private String errorMsg;
	
	private void setUUID() throws Exception {
		if(!obj.has("UUID")) {
			throw new Exception("UUID was not specified");
		}
		this.UUID = obj.getInt("UUID");
	}
	
	private void setKeys() {
		try {
			setUUID();
			//setPublicKey();
		} catch(Exception e) {
			this.valid = false;
			this.errorMsg = e.getMessage();
		}
	}
	
	public ProfileRequest(String req){
		this.valid = true;
		this.errorMsg = "";
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public int getUUID() {
		return UUID;
	}
	
	public String getPublicKey() {
		return publicKey;
	}

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public boolean isValid() {
		return valid;
	}
}
