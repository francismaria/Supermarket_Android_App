package com.acme.server.validation;

import org.json.JSONObject;

public class ContactRequest extends Request {
	private int UUID;
	private String msg;
	private JSONObject obj;
	
	private void setUUID() throws Exception {
		if(!obj.has("UUID")) {
			throw new Exception("UUID was not specified");
		}
		UUID = obj.getInt("UUID");
	}
	
	private void setName() throws Exception {
		if(!obj.has("msg")) {
			throw new Exception("msg was not specified");
		}
		msg = obj.getString("msg");
	}
	
	private void setKeys() {
		try {
			setUUID();
			setName();
		} catch (Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public ContactRequest(String req) {
		setValid(true);
		setErrorMsg("");
		obj = new JSONObject(req);
		
		setKeys();
	}

	public int getUUID() {
		return UUID;
	}
	
	public String getMsg() {
		return msg;
	}
	
}
