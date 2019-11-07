package com.acme.server.validation;

import org.json.JSONObject;

public class HistoryRequest extends Request {
	
	private int UUID;
	private String publicKey;
	
	private JSONObject obj;
	
	private void setUUID() {
		this.UUID = obj.getInt("UUID");
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
	
	public int getUUID() {
		return UUID;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
}
