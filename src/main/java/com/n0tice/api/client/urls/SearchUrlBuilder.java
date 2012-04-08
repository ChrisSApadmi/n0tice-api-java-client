package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

public class SearchUrlBuilder {

	private static final String UTF_8 = "UTF-8";	
	private static final String SEARCH = "/search";
   
	private static Joiner COMMA_JOINER = Joiner.on(",");
   
	final private String apiUrl;

	private Integer page = null;
	private Integer limit = null;
	private List<String> tags = new ArrayList<String>();
	
	public SearchUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
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
	
	public SearchUrlBuilder tags(List<String> tags) {
		this.tags = tags;
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
		if (!tags.isEmpty()) {
			url.append("?tags=" + COMMA_JOINER.join(tags));
		}
		return url.toString();
	}
	
}