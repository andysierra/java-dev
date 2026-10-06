package com.andysierra.response;

public interface BaseResponse {
	
	boolean getSuccess();
	void 	setSuccess(boolean success);
	
	int 	getStatusCode();
	void 	setStatusCode(int statusCode);
	
	String 	getMessage();
	void 	setMessage(String message);
}
