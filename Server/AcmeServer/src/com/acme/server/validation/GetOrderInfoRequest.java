package com.acme.server.validation;

import org.json.JSONObject;

public class GetOrderInfoRequest extends Request {
	
	private int UUID;
	private int orderID;
	private String publicKey;
	
	private JSONObject obj;
	
	private void setUUID() throws Exception {
		if(!obj.has("UUID")) {
			throw new Exception("UUID was not specified");
		}
		this.UUID = obj.getInt("UUID");
	}
	
	private void setOrderID() throws Exception{
		if(!obj.has("orderID")) {
			throw new Exception("orderID was not specified");
		}
		this.orderID = obj.getInt("orderID");
	}
	
	private void setKeys() {
		try {
			setUUID();
			setOrderID();
			//setPublicKey();
		} catch(Exception e) {
			setAsInvalid(e.getMessage());
		}
	}
	
	public GetOrderInfoRequest(String req){
		setValid(true);
		setErrorMsg("");
		this.obj = new JSONObject(req);
		setKeys();
	}
	
	public int getUUID() {
		return UUID;
	}
	
	public int getOrderID() {
		return orderID;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
}