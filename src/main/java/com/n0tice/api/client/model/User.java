package com.n0tice.api.client.model;

import java.util.List;

public class User {
	
	private final String username;
	private String displayName;
	private String bio;
	private Image profileImage;
	private final List<String> noticeboards;
	private final List<String> followedNoticeboards;
	private final List<User> followedUsers;
	
	public User(String username, String displayName, String bio, Image profileImage, List<String> noticeboards, List<String> followedNoticeboards, List<User> followedUsers) {
		this.username = username;
		this.displayName = displayName;
		this.bio = bio;
		this.profileImage = profileImage;
		this.noticeboards = noticeboards;
		this.followedNoticeboards = followedNoticeboards;
		this.followedUsers = followedUsers;
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
	
	public List<String> getNoticeboards() {
		return noticeboards;
	}
	
	public List<String> getFollowedNoticeboards() {
		return followedNoticeboards;
	}
	
	public List<User> getFollowedUsers() {
		return followedUsers;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", displayName=" + displayName
				+ ", bio=" + bio + ", profileImage=" + profileImage
				+ ", noticeboards=" + noticeboards + ", followedNoticeboards="
				+ followedNoticeboards + "]";
	}
	
}
