package com.acme.server.validation;

import java.util.UUID;

import org.json.JSONObject;

public class HistoryRequest extends Request {
	
	private UUID UUID;
	private String publicKey;
	
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
			//setPublicKey();
		} catch(Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public HistoryRequest(String req){
		setValid(true);
		setErrorMsg("");
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public UUID getUUID() {
		return this.UUID;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
}
