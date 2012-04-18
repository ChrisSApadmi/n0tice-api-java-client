package com.n0tice.api.client;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.oauth.N0ticeOauthApi;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.urls.SearchUrlBuilder;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApi {
	
	private static final String UTF_8 = "UTF-8";
	
	private final String apiUrl;	
	private Token accessToken;
	private final UrlBuilder urlBuilder;
	private final HttpFetcher httpFetcher;
	private final SearchParser searchParser;

	private OAuthService service;
	
	public N0ticeApi(String apiUrl) {
		this.apiUrl = apiUrl;
		this.accessToken = null;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
	}
	
	public N0ticeApi(String apiUrl, String consumerKey, String consumerSecret, Token accessToken) {
		this.apiUrl = apiUrl;
		this.accessToken = accessToken;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
		service = new ServiceBuilder().provider(new N0ticeOauthApi(apiUrl))
			.apiKey(consumerKey)
			.apiSecret(consumerSecret)
			.build();		
	}
	
	public N0ticeApi(String apiUrl, UrlBuilder urlBuilder, HttpFetcher httpFetcher, SearchParser searchParser) {
		this.apiUrl = apiUrl;
		this.urlBuilder = urlBuilder;
		this.httpFetcher = httpFetcher;
		this.searchParser = searchParser;
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
	
	public ResultSet search(SearchQuery query) throws ParsingException, HttpFetchException {
		SearchUrlBuilder searchUrlBuilder = new SearchUrlBuilder(apiUrl);
		if (query.getPage() != null) {
			searchUrlBuilder.page(query.getPage());
		}
		if (query.getLimit() != null) {
			searchUrlBuilder.limit(query.getLimit());
		}
		if (!query.getTags().isEmpty()) {
			searchUrlBuilder.tags(query.getTags());
		}
		return searchParser.parseSearchResults(httpFetcher.fetchContent(searchUrlBuilder.toUrl(), UTF_8));
	}

	public Content postRepost(String headline, double latitude, double longitude, String body) throws ParsingException, AuthorisationException {		
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/report/new");	
		request.addBodyParameter("headline", headline);
		request.addBodyParameter("latitude", Double.toString(latitude));
		request.addBodyParameter("longitude", Double.toString(longitude));
		request.addBodyParameter("body", body);
		service.signRequest(accessToken, request);
	    
		Response response = request.send();
		
		if (response.getCode() == 200) {
			final String responseBody = response.getBody();
	    	return searchParser.parseReport(responseBody);
		}
	
		if (response.getCode() == 401) {
			throw new AuthorisationException();
		}
		
		throw new RuntimeException();
	}

	public Content updateReport(String id, String headline, String body) throws ParsingException, AuthorisationException {	
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id);	
		request.addBodyParameter("headline", headline);
		service.signRequest(accessToken, request);
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
	
		if (response.getCode() == 401) {
			System.out.println(responseBody);
			throw new AuthorisationException();
		}
		
		throw new RuntimeException();		
	}
	
}
