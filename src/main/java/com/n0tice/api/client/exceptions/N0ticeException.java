package com.n0tice.api.client.exceptions;

public class N0ticeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public N0ticeException() {
		super();
	}
	
	public N0ticeException(String message) {
		super();
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBody() {
		return message;
	}

	@Override
	public String toString() {
		return "N0ticeException [message=" + message + "]";
	}
	
}
