package com.n0tice.api.client;

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApi {
	
	private static final String UTF_8 = "UTF-8";
	
	final private UrlBuilder urlBuilder;
	final private HttpFetcher httpFetcher;
	final private SearchParser searchParser;
	
	private int page = 1;
	
	public N0ticeApi(String apiUrl) {
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();	
	}
	
	public N0ticeApi(UrlBuilder urlBuilder, HttpFetcher httpFetcher, SearchParser searchParser) {
		this.urlBuilder = urlBuilder;
		this.httpFetcher = httpFetcher;
		this.searchParser = searchParser;	
	}
	
	public ResultSet latest() throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.page(page).latest(), UTF_8));
	}

	public ResultSet near(String locationName) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.near(locationName), UTF_8));
	}
	
	public ResultSet near(double latitude, double longitude) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.near(latitude, longitude), UTF_8));
	}
	
	public ResultSet user(String userName) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.user(userName), UTF_8));
	}

	public ResultSet type(String type) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.type(type), UTF_8));
	}
	
	public ResultSet noticeboard(String noticeBoard) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.noticeboard(noticeBoard), UTF_8));
	}
	
	public ResultSet tag(String tag)  throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.tag(tag), UTF_8));
	}
	
	public Content get(String id) throws HttpFetchException, ParsingException {
		return searchParser.parseReport(httpFetcher.fetchContent(urlBuilder.get(id), UTF_8));
	}

	public N0ticeApi page(int page) {
		this.page = page;
		return this;
	}
	
}
