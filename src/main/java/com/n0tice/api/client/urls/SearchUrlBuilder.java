package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchUrlBuilder {

	private static final String UTF_8 = "UTF-8";

	private static final String SEARCH = "/search";

	final private String apiUrl;

	private Integer page = null;

	private Integer limit = null;

	public SearchUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
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
	
	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public SearchUrlBuilder page(int page) {
		this.page = page;
		return this;
	}

	public SearchUrlBuilder limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public String toUrl() {
		StringBuilder url = new StringBuilder();
		url.append(apiUrl);
		url.append(SEARCH);
		if (page != null) {
			url.append("?page=" + page);
		}
		if (limit != null) {
			url.append("?limit=" + limit);
		}
		return url.toString();
	}
	
}
