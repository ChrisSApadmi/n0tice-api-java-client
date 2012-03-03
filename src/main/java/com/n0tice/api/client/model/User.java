package com.n0tice.api.client.model;

public class User {
	
	private final String username;
	private String displayName;
	private Image profileImage;
	
	public User(String username, String displayName, Image profileImage) {
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

	public Image getProfileImage() {
		return profileImage;
	}

	@Override
	public String toString() {
		return "User [displayName=" + displayName + ", profileImage="
				+ profileImage + ", username=" + username + "]";
	}
	
}
