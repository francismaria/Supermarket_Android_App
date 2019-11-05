package com.acme.server.validation;

public interface RequestValidation {
	
	public String getErrorMsg();

	public boolean isValid();
}
