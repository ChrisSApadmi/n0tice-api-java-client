package com.n0tice.api.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ImageFile;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.model.User;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.parsers.UserParser;
import com.n0tice.api.client.urls.SearchUrlBuilder;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApi {
	
	private static final String UTF_8 = "UTF-8";
	private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	
	private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM);
	
	private final String apiUrl;	
	private final UrlBuilder urlBuilder;
	private final HttpFetcher httpFetcher;
	private final SearchParser searchParser;
	private final HttpClient client;
	
	private String username;
	private String password;
	
	public N0ticeApi(String apiUrl) {
		this.apiUrl = apiUrl;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
		client = new DefaultHttpClient();

	}
	
	public N0ticeApi(String apiUrl, String username, String password) {
		this.apiUrl = apiUrl;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
		
		this.username = username;
		this.password = password;
		
		client = new DefaultHttpClient();
	}
	
	public N0ticeApi(String apiUrl, UrlBuilder urlBuilder, HttpFetcher httpFetcher, SearchParser searchParser) {
		this.apiUrl = apiUrl;
		this.urlBuilder = urlBuilder;
		this.httpFetcher = httpFetcher;
		this.searchParser = searchParser;
		client = new DefaultHttpClient();
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
	
	public Content get(String id) throws HttpFetchException, NotFoundException, ParsingException {
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
		if (query.getNoticeBoard() != null) {
			searchUrlBuilder.noticeBoard(query.getNoticeBoard());
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
	
	public Content postReport(String headline, double latitude, double longitude, String body, String link, ImageFile image, String noticeboard) throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException, BadRequestException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/report/new");
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (headline != null) {
			entity.addPart("headline", new StringBody(headline, Charset.forName("UTF-8")));
		}
		if (noticeboard != null) {
			entity.addPart("noticeboard", new StringBody(noticeboard, Charset.forName("UTF-8")));
		}
		entity.addPart("latitude", new StringBody(Double.toString(latitude), Charset.forName("UTF-8")));
		entity.addPart("longitude", new StringBody(Double.toString(longitude), Charset.forName("UTF-8")));
		populateUpdateFields(body, link, image, entity);
		post.setEntity(entity);		
		authenticateRequest(post);
				
		HttpResponse response = client.execute(post);		
		if (response.getStatusLine().getStatusCode() == 200) {
			final String responseBody = IOUtils.toString(response.getEntity().getContent());
	    	return searchParser.parseReport(responseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public Content postEvent(String headline, double latitude, double longitude, String body, String link, ImageFile image, String noticeboard, LocalDateTime startDate, LocalDateTime endDate) throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException, BadRequestException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/event/new");
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (headline != null) {
			entity.addPart("headline", new StringBody(headline, Charset.forName("UTF-8")));
		}
		if (noticeboard != null) {
			entity.addPart("noticeboard", new StringBody(noticeboard, Charset.forName("UTF-8")));
		}
		entity.addPart("latitude", new StringBody(Double.toString(latitude), Charset.forName("UTF-8")));
		entity.addPart("longitude", new StringBody(Double.toString(longitude), Charset.forName("UTF-8")));
		populateUpdateFields(body, link, image, entity);
		
		entity.addPart("startDate", new StringBody(startDate.toString(dateFormatter), Charset.forName("UTF-8")));
		entity.addPart("endDate", new StringBody(endDate.toString(dateFormatter), Charset.forName("UTF-8")));

		post.setEntity(entity);
		authenticateRequest(post);

		HttpResponse response = client.execute(post);		
		if (response.getStatusLine().getStatusCode() == 200) {
			final String responseBody = IOUtils.toString(response.getEntity().getContent());
	    	return searchParser.parseReport(responseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public void postReportUpdate(String reportId, String body, String link, ImageFile image) throws IOException, AuthorisationException, NotFoundException, NotAllowedException, BadRequestException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/" + reportId  + "/update/new");
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		populateUpdateFields(body, link, image, entity);
		post.setEntity(entity);
		authenticateRequest(post);

		HttpResponse response = client.execute(post);		
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean voteInteresting(String id) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {		
		HttpPost post = new HttpPost(apiUrl + "/" + id + "/vote/interesting");		
		authenticateRequest(post);

		HttpResponse response = client.execute(post);	
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}

		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean repost(String id, String noticeboard) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/" + id + "/repost");		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("noticeboard", new StringBody(noticeboard, Charset.forName("UTF-8")));
		post.setEntity(entity);
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}

		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public int interestingVotes(String id) throws NotFoundException, ParsingException, NotAllowedException, AuthorisationException, BadRequestException, IllegalStateException, IOException {
		HttpGet get = new HttpGet(apiUrl + "/" + id + "/votes/interesting");
		
		HttpResponse response = client.execute(get);		
		if (response.getStatusLine().getStatusCode() == 200) {
			return searchParser.parseVotes(IOUtils.toString(response.getEntity().getContent()));
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public Content updateReport(String id, String headline, String body) throws ParsingException, AuthorisationException, NotFoundException, NotAllowedException, BadRequestException, IllegalStateException, IOException, AuthenticationException {	
		HttpPost post = new HttpPost(apiUrl + "/" + id);	
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("headline", new StringBody(headline, Charset.forName("UTF-8")));
		post.setEntity(entity);
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			return searchParser.parseReport(IOUtils.toString(response.getEntity().getContent()));
		}
		
		handleExceptions(response);
		throw new RuntimeException();		
	}
	
	public boolean followUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/user/" + username + "/follow");	
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean unfollowUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/user/" + username + "/unfollow");	
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean followNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/noticeboard/" + noticeboard + "/follow");	
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean unfollowNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {
		HttpPost post = new HttpPost(apiUrl + "/noticeboard/" + noticeboard + "/unfollow");	
		authenticateRequest(post);

		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			response.getEntity().consumeContent();
	    	return true;
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public User createUser(String username, String password, String email) throws ParsingException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, IllegalStateException, IOException, AuthenticationException {		
		HttpPost post = new HttpPost(apiUrl + "/user/new");	
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("username", new StringBody(username, Charset.forName("UTF-8")));
		entity.addPart("password", new StringBody(password, Charset.forName("UTF-8")));
		entity.addPart("email", new StringBody(email, Charset.forName("UTF-8")));
		post.setEntity(entity);
		
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			final String responseBody = IOUtils.toString(response.getEntity().getContent());
			return new UserParser().parseUserProfile(responseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}

	public User updateUserDetails(String username, String displayName, String bio, ImageFile image) throws ParsingException, IOException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, AuthenticationException {		
		HttpPost post = new HttpPost( apiUrl + "/user/" + username);	
		
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
		post.setEntity(entity);
		authenticateRequest(post);

		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			final String responseBody = IOUtils.toString(response.getEntity().getContent());
			return new UserParser().parseUserProfile(responseBody);
		}
		
		handleExceptions(response);
		throw new RuntimeException();
	}
	
	public boolean deleteReport(String id) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, ClientProtocolException, IOException, AuthenticationException {		
		HttpDelete delete = new HttpDelete(apiUrl + "/" + id);	
		authenticateRequest(delete);

		HttpResponse response = client.execute(delete);
		if (response.getStatusLine().getStatusCode() == 200) {
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
	
	private void handleExceptions(HttpResponse response) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException {
		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 404) {
			throw new NotFoundException();
		}
		if (statusCode == 403) {
			throw new NotAllowedException();
		}		
		if (statusCode == 401) {
			throw new AuthorisationException();
		}
		if (statusCode == 400) {
			throw new BadRequestException();
		}
		
		try {
			System.err.println(statusCode + ": " + IOUtils.toString(response.getEntity().getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException();
	}
	
	private void authenticateRequest(HttpRequestBase request) throws AuthenticationException {
		BasicScheme scheme = new BasicScheme();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);		
		Header authenticate = scheme.authenticate(credentials, request);
		request.addHeader(authenticate);
	}
	
}
