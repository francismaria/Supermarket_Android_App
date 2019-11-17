package com.acme.server.validation;

import org.json.JSONObject;

import java.util.UUID;

public class ProfileRequest extends Request{
	private UUID UUID;
	
	private JSONObject obj;
	
	private void setUUID() throws Exception {
		if(!obj.has("UUID")) {
			throw new Exception("UUID was not specified");
		}
		this.UUID = java.util.UUID.fromString(obj.getString("UUID"));
	}
	
	private void setKeys() {
		try {
			setUUID();
		} catch(Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public ProfileRequest(String req){
		setValid(true);
		setErrorMsg("");
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public UUID getUUID() {
		return UUID;
	}
}
