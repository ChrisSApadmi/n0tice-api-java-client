package com.n0tice.api.client.urls;


public class UrlBuilder {

	final private String apiUrl;
	
	public UrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String get(String id) {
		return apiUrl + "/" + id;
	}
	
	public String userProfile(String username) {
		return apiUrl + "/user/" + username;
	}
	
	public String userNotifications(String username) {
		return userProfile(username) + "/notifications";
	}
	
	public String noticeBoard(String noticeboard) {
		return apiUrl + "/noticeboard/" + noticeboard;
	}
	
	public String userFollowedUsers(String username) {
		return userProfile(username) + "/following/users";
	}
	
	public String userFollowedNoticeboards(String username) {
		return userProfile(username) + "/following/noticeboards";
	}

	public String userNoticeboards(String username) {
		return userProfile(username) + "/noticeboards";
	}
	
}
