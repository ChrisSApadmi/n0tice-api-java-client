package com.n0tice.api.client.model;

public class User {
	
	private final String username;
	private String displayName;
	private String bio;
	private Image profileImage;
	
	public User(String username, String displayName, String bio, Image profileImage) {
		this.username = username;
		this.displayName = displayName;
		this.bio = bio;
		this.profileImage = profileImage;
	}
	
	public String getUsername() {
		return username;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getBio() {
		return bio;
	}

	public Image getProfileImage() {
		return profileImage;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", displayName=" + displayName + ", bio=" + bio + ", profileImage=" + profileImage + "]";
	}
	
}
