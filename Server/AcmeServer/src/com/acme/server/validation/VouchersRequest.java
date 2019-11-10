package com.acme.server.validation;

import org.json.JSONObject;

public class VouchersRequest extends Request {
	private int UUID;
	private String publicKey;
	
	private JSONObject obj;
	
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
			setAsInvalid(e.getMessage());
		}
	}
	
	public VouchersRequest(String req){
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
