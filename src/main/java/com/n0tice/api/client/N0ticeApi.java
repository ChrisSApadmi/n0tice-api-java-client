package com.n0tice.api.client;

import java.util.List;

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;


public class N0ticeApi {
	
	private static final String UTF_8 = "UTF-8";
	
	final private UrlBuilder urlBuilder;
	final private HttpFetcher httpFetcher;
	final private SearchParser searchParser;
	
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
	
	public List<Content> latest() throws HttpFetchException, ParsingException {
		return searchParser.parse(httpFetcher.fetchContent(urlBuilder.latest(), UTF_8));
	}

	public List<Content> near(String locationName) throws HttpFetchException, ParsingException {
		return searchParser.parse(httpFetcher.fetchContent(urlBuilder.near(locationName), UTF_8));
	}

	public List<Content> user(String userName) throws HttpFetchException, ParsingException {
		return searchParser.parse(httpFetcher.fetchContent(urlBuilder.user(userName), UTF_8));
	}
	
}
