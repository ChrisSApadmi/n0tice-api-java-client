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

	private String q = null;
	private Integer page = null;
	private Integer limit = null;
	private List<String> tags = new ArrayList<String>();
	private String type= null;
	private String location = null;
	private String noticeBoard;
	
	public SearchUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public SearchUrlBuilder q(String q) {
		this.q = q;
		return this;		
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
	
	public SearchUrlBuilder type(String type) {
		this.type = type;
		return this;		
	}
	
	public SearchUrlBuilder noticeBoard(String noticeBoard) {
		this.noticeBoard = noticeBoard;
		return this;		
	}
	
	public void location(String location) {
		this.location = location;		
	}
	
	public String toUrl() {	// TODO won't build multi term queries correctly
		StringBuilder url = new StringBuilder();
		url.append(apiUrl);
		url.append(SEARCH);

		String joiner = "?";
		if (q != null) {
			url.append(joiner);
			url.append("q=" + urlEncode(q));
			joiner = "&";
		}		
		if (page != null) {
			url.append(joiner);
			url.append("page=" + page);
			joiner = "&";
		}
		if (limit != null) {
			url.append(joiner);
			url.append("limit=" + limit);
			joiner = "&";
		}
		if (type != null) {
			url.append(joiner);
			url.append("type=" + urlEncode(type));
			joiner = "&";
		}
		if (noticeBoard != null) {
			url.append(joiner);
			url.append("noticeboard=" + urlEncode(noticeBoard));
			joiner = "&";
		}
		if (!tags.isEmpty()) {
			url.append(joiner);
			url.append("tags=" + COMMA_JOINER.join(tags));	// TODO how is this to be encoded
			joiner = "&";
		}
		if (location != null) {
			url.append(joiner);
			url.append("location=" + urlEncode(location));
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
