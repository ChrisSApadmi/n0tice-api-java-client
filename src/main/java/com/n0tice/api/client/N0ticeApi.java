package com.n0tice.api.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ImageFile;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.model.User;
import com.n0tice.api.client.oauth.N0ticeOauthApi;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.parsers.UserParser;
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
	
	public ResultSet near(double latitude, double longitude) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.near(latitude, longitude), UTF_8));
	}
	
	public ResultSet user(String userName) throws HttpFetchException, ParsingException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(urlBuilder.user(userName), UTF_8));
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
		if (query.getType() != null) {
			searchUrlBuilder.type(query.getType());
		}
		if (query.getLocation() != null) {
			searchUrlBuilder.location(query.getLocation());
		}
		return searchParser.parseSearchResults(httpFetcher.fetchContent(searchUrlBuilder.toUrl(), UTF_8));
	}
	
	public User userProfile(String username) throws NotFoundException, ParsingException, HttpFetchException {
		return searchParser.parseUserResult(httpFetcher.fetchContent(urlBuilder.userProfile(username), UTF_8));
	}
	
	public String noticeBoard(String noticeboard) throws NotFoundException, ParsingException, HttpFetchException {
		return searchParser.parseNoticeboardResult((httpFetcher.fetchContent(urlBuilder.noticeBoard(noticeboard), UTF_8)));
	}
	
	public Content postReport(String headline, double latitude, double longitude, String body, String link, ImageFile image, String noticeboard) throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/report/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (headline != null) {
			entity.addPart("headline", new StringBody(headline, Charset.forName("UTF-8")));
		}
		if (noticeboard != null) {
			entity.addPart("noticeboard", new StringBody(headline, Charset.forName("UTF-8")));
		}
		entity.addPart("latitude", new StringBody(Double.toString(latitude), Charset.forName("UTF-8")));
		entity.addPart("longitude", new StringBody(Double.toString(longitude), Charset.forName("UTF-8")));
		populateUpdateFields(body, link, image, entity);
		
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		service.signRequest(accessToken, request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public void postReportUpdate(String reportId, String body, String link, ImageFile image) throws IOException, AuthorisationException, NotFoundException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + reportId  + "/update/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		populateUpdateFields(body, link, image, entity);

		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean voteInteresting(String id) throws NotFoundException, AuthorisationException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/vote/interesting");	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}

		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public int interestingVotes(String id) throws NotFoundException, ParsingException, NotAllowedException, AuthorisationException {
		OAuthRequest request = new OAuthRequest(Verb.GET, apiUrl + "/" + id + "/votes/interesting");	
			
		final Response response = request.send();
		
		if (response.getCode() == 200) {
			return searchParser.parseVotes(response.getBody());
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public Content updateReport(String id, String headline, String body) throws ParsingException, AuthorisationException, NotFoundException, NotAllowedException {	
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id);	
		request.addBodyParameter("headline", headline);
		service.signRequest(accessToken, request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
	
		handleExceptions(response);
		throw new RuntimeException();		
	}
	
	public boolean followUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username + "/follow");	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean unfollowUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username + "/unfollow");	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean followNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/noticeboard/" + noticeboard + "/follow");	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean unfollowNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/noticeboard/" + noticeboard + "/unfollow");	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public User createUser(String username, String password, String email) throws ParsingException, NotFoundException, NotAllowedException, AuthorisationException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/new");
		request.addBodyParameter("username", username);		
		request.addBodyParameter("password", password);
		request.addBodyParameter("email", password);
		
		final Response response = request.send();

		final String repsonseBody = response.getBody();
		if (response.getCode() == 200) {
			return new UserParser().parseUserProfile(repsonseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}

	public User updateUserDetails(String username, String displayName, String bio, ImageFile image) throws ParsingException, IOException, NotFoundException, NotAllowedException, AuthorisationException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username);		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (displayName != null) {
			entity.addPart("displayName", new StringBody(displayName, Charset.forName("UTF-8")));
		}
		if (bio != null) {
			entity.addPart("bio", new StringBody(bio, Charset.forName("UTF-8")));
		}
		if (image != null) {
			entity.addPart("image", new ByteArrayBody(image.getData(), image.getFilename()));
		}
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		service.signRequest(accessToken, request);
		
		Response response = request.send();

		final String repsonseBody = response.getBody();
		if (response.getCode() == 200) {
			return new UserParser().parseUserProfile(repsonseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean deleteReport(String id) throws NotFoundException, NotAllowedException, AuthorisationException {
		OAuthRequest request = new OAuthRequest(Verb.DELETE, apiUrl + "/" + id);	
		service.signRequest(accessToken, request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
			return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();		
	}
	
	private void populateUpdateFields(String body, String link,
			ImageFile image, MultipartEntity entity)
			throws UnsupportedEncodingException {
		if (body != null) {
			entity.addPart("body", new StringBody(body, Charset.forName("UTF-8")));
		}
		if (link != null) {
			entity.addPart("link", new StringBody(link, Charset.forName("UTF-8")));
		}
		if (image != null) {
			entity.addPart("image", new ByteArrayBody(image.getData(), image.getFilename()));
		}
	}
	
	private byte[] extractMultpartBytes(MultipartEntity entity) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		entity.writeTo(byteArrayOutputStream);			
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		return byteArray;
	}
	
	private void handleExceptions(Response response) throws NotFoundException, NotAllowedException, AuthorisationException {
		if (response.getCode() == 404) {
			throw new NotFoundException();
		}
		if (response.getCode() == 403) {
			throw new NotAllowedException();
		}		
		if (response.getCode() == 401) {
			throw new AuthorisationException();
		}
		
		System.err.println(response.getCode() + ": " + response.getBody());
		throw new RuntimeException();
	}
	
}
