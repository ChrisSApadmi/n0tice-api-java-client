package com.n0tice.api.client.model;

public class AccessToken {

	private final String token;
	private final String secret;

	public AccessToken(String token, String secret) {
		this.token = token;
		this.secret = secret;
	}
	
	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}
	
}
