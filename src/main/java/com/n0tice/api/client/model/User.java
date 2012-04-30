package com.n0tice.api.client.model;

import java.util.List;

public class User {
	
	private String username;
	private String displayName;
	private String bio;
	private Image profileImage;
	private List<String> noticeboards;
	private List<String> followedNoticeboards;
	private List<User> followedUsers;
	
	public User(String username) {
		this.username = username;
	}

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", displayName=" + displayName
				+ ", bio=" + bio + ", profileImage=" + profileImage
				+ ", noticeboards=" + noticeboards + ", followedNoticeboards="
				+ followedNoticeboards + "]";
	}
	
}
