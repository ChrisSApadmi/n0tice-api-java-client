package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlBuilder {

	private static final String UTF_8 = "UTF-8";

	private static final String SEARCH = "/search";

	final private String apiUrl;

	public UrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String latest() {
		return apiUrl + SEARCH;
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
		return apiUrl + SEARCH  + "?contentType=" + urlEncode(type);
	}
	
	public String noticeboard(String noticeBoard) {
		return apiUrl + SEARCH  + "?domain=" + urlEncode(noticeBoard);
	}
	
	public String get(String id) {
		return apiUrl + id;
	}
	
	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}
	
}
