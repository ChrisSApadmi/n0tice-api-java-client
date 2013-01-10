package com.n0tice.api.client.exceptions;

public class N0ticeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final String body;

	public N0ticeException(String body) {
		this.body = body;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBody() {
		return body;
	}

	@Override
	public String toString() {
		return "N0ticeException [body=" + body + "]";
	}
	
}
