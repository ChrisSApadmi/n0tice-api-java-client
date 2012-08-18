package com.n0tice.api.client.model;

public class NewUserResponse {

	private final User user;
	private final AccessToken accessToken;

	public NewUserResponse(User user, AccessToken accessToken) {
		this.user = user;
		this.accessToken = accessToken;
	}
	
	public User getUser() {
		return user;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}
	
}
