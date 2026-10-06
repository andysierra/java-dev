package com.andysierra.response.impl;

import com.andysierra.entity.impl.Usuario;
import com.andysierra.response.BaseResponse;

// @Bean?
public class UsuarioCreateResponse implements BaseResponse {

	public boolean 	success 	= false;
	public int 		statusCode 	= 0;
	public String 	message 	= "";
	
	public Usuario usuario;
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	@Override
	public String toString() {
		return "UsuarioCreateResponse [success=" + success + ", statusCode=" + statusCode + ", message=" + message
				+ ", usuario=" + usuario + "]";
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean getSuccess() {
		return success;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

}
