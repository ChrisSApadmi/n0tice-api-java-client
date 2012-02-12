package com.n0tice.api.client.model;

public class User {
	
	private final String username;
	private String displayName;
	private String profileImage;
	
	public User(String username, String displayName, String profileImage) {
		this.username = username;
		this.displayName = displayName;
		this.profileImage = profileImage;
	}
	
	public String getUsername() {
		return username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	@Override
	public String toString() {
		return "User [displayName=" + displayName + ", profileImage="
				+ profileImage + ", username=" + username + "]";
	}
	
}
