package com.n0tice.api.client.model;

public class NewUserResponse {

	private final User user;
	private final String token;
	private final String secret;

	public NewUserResponse(User user, String token, String secret) {
		this.user = user;		
		this.token = token;
		this.secret = secret;
	}

	public User getUser() {
		return user;
	}

	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}
	
}
