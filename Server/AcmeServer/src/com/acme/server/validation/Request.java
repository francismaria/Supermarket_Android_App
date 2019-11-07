package com.acme.server.validation;

public abstract class Request {

	// Validates the request
	private boolean valid;
	
	private String errorMsg;
	
	protected void setErrorMsg(String msg) {
		this.errorMsg = msg;
	}
	
	protected void setValid(boolean valid) {
		this.valid = valid;
	}
	
	protected void setAsInvalid(String msg) {
		this.valid = false;
		this.errorMsg = msg;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isValid() {
		return valid;
	}
}
