package com.n0tice.api.client.exceptions;

public class MissingCredentialsExeception extends N0ticeException {
	
	private static final long serialVersionUID = 1L;

	public MissingCredentialsExeception() {
		super("The requested API action requires authentication credientals to be supplied");
	}
	
}
