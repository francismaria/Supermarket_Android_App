package com.acme.server.validation;

import org.json.JSONObject;

public class VerifyUsernameRequest extends Request{

	private String username;
	private JSONObject obj;
	
	private void setUsername() throws Exception {
		if(!obj.has("username")) {
			throw new Exception("Username was not specified");
		}
		username = obj.getString("username");
	}
	
	private void setKeys() {
		try {
			setUsername();
		} catch (Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public VerifyUsernameRequest(String req) {
		setValid(true);
		setErrorMsg("");
		obj = new JSONObject(req);
		
		setKeys();
	}

	public String getUsername() {
		return username;
	}
}
