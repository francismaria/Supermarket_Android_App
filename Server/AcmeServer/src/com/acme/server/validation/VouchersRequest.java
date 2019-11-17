package com.acme.server.validation;

import org.json.JSONObject;

import java.util.UUID;

public class VouchersRequest extends Request {
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
		} catch(Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public VouchersRequest(String req){
		setValid(true);
		setErrorMsg("");
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public UUID getUUID() {
		return UUID;
	}

	public String getPublicKey() {
		return publicKey;
	}
}
