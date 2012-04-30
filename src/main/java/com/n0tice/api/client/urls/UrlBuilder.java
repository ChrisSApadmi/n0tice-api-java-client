package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlBuilder {

	private static final String UTF_8 = "UTF-8";

	private static final String SEARCH = "/search";

	final private String apiUrl;

	private int page = 0;

	public UrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String latest() {
		String url = apiUrl + SEARCH;
		if (page > 0) {
			url = url + "?page=" + page;
		}
		return url;
	}

	public String near(String location) {
		return apiUrl + SEARCH  + "?location=" + urlEncode(location);
	}
	
	public String near(double latitude, double longitude) {
		return apiUrl + SEARCH  + "?latitude=" + latitude + "&longitude=" + longitude;
	}
	
	public String user(String username) {
		return apiUrl + SEARCH  + "?user=" + urlEncode(username);
	}
	
	public String type(String type) {
		return apiUrl + SEARCH  + "?type=" + urlEncode(type);
	}
	
	public String noticeboard(String noticeBoard) {
		return apiUrl + SEARCH  + "?noticeboard=" + urlEncode(noticeBoard);
	}
	
	public String tag(String tag) {
		return apiUrl + SEARCH  + "?tags=" + urlEncode(tag);
	}
	
	public String get(String id) {
		return apiUrl + "/" + id;
	}
	
	public String userProfile(String username) {
		return apiUrl + "/user/" + username;
	}
	
	public String noticeBoard(String noticeboard) {
		return apiUrl + "/noticeboard/" + noticeboard;
	}
	
	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public UrlBuilder page(int page) {
		this.page = page;
		return this;
	}
	
}
