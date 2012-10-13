package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.common.base.Joiner;
import com.n0tice.api.client.model.SearchQuery;

public class SearchUrlBuilder {

	private static final String UTF_8 = "UTF-8";	
	private static final String SEARCH = "/search";
   
	private static Joiner COMMA_JOINER = Joiner.on(",");
   
	final private String apiUrl;
	
	public SearchUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String toUrl(SearchQuery searchQuery) {
		final StringBuilder url = new StringBuilder();
		url.append(apiUrl);
		url.append(SEARCH);

		String joiner = "?";
		if (searchQuery.getQ() != null) {
			url.append(joiner);
			url.append("q=" + urlEncode(searchQuery.getQ()));
			joiner = "&";
		}		
		if (searchQuery.getPage() != null) {
			url.append(joiner);
			url.append("page=" + searchQuery.getPage());
			joiner = "&";
		}
		if (searchQuery.getLimit() != null) {
			url.append(joiner);
			url.append("limit=" + searchQuery.getLimit());
			joiner = "&";
		}
		if (searchQuery.getType() != null) {
			url.append(joiner);
			url.append("type=" + urlEncode(searchQuery.getType()));
			joiner = "&";
		}
		if (searchQuery.getNoticeBoard() != null) {
			url.append(joiner);
			url.append("noticeboard=" + urlEncode(searchQuery.getNoticeBoard()));
			joiner = "&";
		}
		if (searchQuery.getUser() != null) {
			url.append(joiner);
			url.append("user=" + urlEncode(searchQuery.getUser()));
			joiner = "&";
		}		
		if (!searchQuery.getTags().isEmpty()) {
			url.append(joiner);
			url.append("tags=" + urlEncode(COMMA_JOINER.join(searchQuery.getTags())));	// TODO how is this to be encoded
			joiner = "&";
		}
		if (searchQuery.getLocation() != null) {
			url.append(joiner);
			url.append("location=" + urlEncode(searchQuery.getLocation()));
			joiner = "&";
		}
		if (searchQuery.getLatitude() != null) {
			url.append(joiner);
			url.append("latitude=" + searchQuery.getLatitude());
			joiner = "&";
		}
		if (searchQuery.getLongitude() != null) {
			url.append(joiner);
			url.append("longitude=" + searchQuery.getLongitude());
			joiner = "&";
		}
		return url.toString();
	}
	
	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, UTF_8);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}
	
}
