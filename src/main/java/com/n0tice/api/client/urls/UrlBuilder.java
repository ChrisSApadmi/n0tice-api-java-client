package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class UrlBuilder {

	private static final String CLOSE = "close";
	private static final String UTF_8 = "UTF-8";
	
	final private String apiUrl;
	
	public UrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String get(String id) {
		return apiUrl + "/" + id;
	}
	
	public String userProfile(String username) {
		return apiUrl + "/user/" + urlEncode(username);
	}
	
	public String userNotifications(String username) {
		return userProfile(username) + "/notifications";
	}
	
	public String noticeBoard(String noticeboard) {
		return apiUrl + "/noticeboard/" + urlEncode(noticeboard);
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

	public String closeNoticeboard(String domain) {
		return noticeBoard(domain) + "/" + CLOSE;
	}
	
	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
