package com.acme.server.validation;

import org.json.JSONObject;

import java.util.UUID;

public class ContactRequest extends Request {
	private UUID UUID;
	private String msg;
	private JSONObject obj;
	
	private void setUUID() throws Exception {
		if(!obj.has("UUID")) {
			throw new Exception("UUID was not specified");
		}
		UUID = java.util.UUID.fromString(obj.getString("UUID"));
	}
	
	private void setMsg() throws Exception {
		if(!obj.has("msg")) {
			throw new Exception("msg was not specified");
		}
		msg = obj.getString("msg");
	}
	
	private void setKeys() {
		try {
			setUUID();
			setMsg();
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

	public UUID getUUID() {
		return UUID;
	}
	
	public String getMsg() {
		return msg;
	}
	
}
