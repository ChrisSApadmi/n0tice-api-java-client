package com.n0tice.api.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.exceptions.BadRequestException;
import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.MissingCredentialsExeception;
import com.n0tice.api.client.exceptions.N0ticeException;
import com.n0tice.api.client.exceptions.NotAllowedException;
import com.n0tice.api.client.exceptions.NotFoundException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.AccessToken;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.Group;
import com.n0tice.api.client.model.MediaFile;
import com.n0tice.api.client.model.MediaType;
import com.n0tice.api.client.model.ModerationComplaintType;
import com.n0tice.api.client.model.NewUserResponse;
import com.n0tice.api.client.model.Noticeboard;
import com.n0tice.api.client.model.Reoccurence;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.model.Update;
import com.n0tice.api.client.model.User;
import com.n0tice.api.client.model.VideoAttachment;
import com.n0tice.api.client.oauth.N0ticeOauthApi;
import com.n0tice.api.client.parsers.NoticeboardParser;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.parsers.UserParser;
import com.n0tice.api.client.urls.SearchUrlBuilder;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApi {
	
	private static Logger log = Logger.getLogger(N0ticeApi.class);
	
	private static final String UTF_8 = "UTF-8";
	private static final String COMMA = ",";	
	private static DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	private static DateTimeFormatter ZULE_TIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
	
	private final String apiUrl;	
	private final UrlBuilder urlBuilder;
	private final SearchUrlBuilder searchUrlBuilder;
	private final HttpFetcher httpFetcher;
	private final SearchParser searchParser;
	private final UserParser userParser;
	private final NoticeboardParser noticeboardParser;

	private OAuthService service;
	private Token scribeAccessToken;
	
	public N0ticeApi(String apiUrl) {
		this.apiUrl = apiUrl;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.searchUrlBuilder = new SearchUrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
		this.userParser = new UserParser();
		this.noticeboardParser = new NoticeboardParser();
	}
	
	public N0ticeApi(String apiUrl, String consumerKey, String consumerSecret, AccessToken accessToken) {
		this.apiUrl = apiUrl;
		this.urlBuilder = new UrlBuilder(apiUrl);
		this.searchUrlBuilder = new SearchUrlBuilder(apiUrl);
		this.httpFetcher = new HttpFetcher();
		this.searchParser = new SearchParser();
		this.userParser = new UserParser();
		this.noticeboardParser = new NoticeboardParser();

		service = new ServiceBuilder().provider(new N0ticeOauthApi(apiUrl))
			.apiKey(consumerKey)
			.apiSecret(consumerSecret)
			.build();
		scribeAccessToken = new Token(accessToken.getToken(), accessToken.getSecret());
	}
	
	public Content get(String id) throws HttpFetchException, NotFoundException, ParsingException {
		return searchParser.parseReport(httpFetcher.fetchContent(urlBuilder.get(id), UTF_8));
	}
	
	public Update getUpdate(String id) throws HttpFetchException, NotFoundException, ParsingException {
		return searchParser.parseUpdate(httpFetcher.fetchContent(urlBuilder.get(id), UTF_8));
	}
	
	public Content authedGet(String id) throws HttpFetchException, NotFoundException, ParsingException, N0ticeException, NotAllowedException {		
		final OAuthRequest request = new OAuthRequest(Verb.GET, urlBuilder.get(id));
		oauthSignRequest(request);
		
		final Response response = request.send();
		if (response.getCode() == 200) {
			return searchParser.parseReport(response.getBody());
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public ResultSet search(SearchQuery searchQuery) throws ParsingException, HttpFetchException {
		return searchParser.parseSearchResults(httpFetcher.fetchContent(searchUrlBuilder.toUrl(searchQuery), UTF_8));
	}
	
	public User user(String username) throws NotFoundException, ParsingException, HttpFetchException {
		return userParser.parseUserProfile(httpFetcher.fetchContent(urlBuilder.userProfile(username), UTF_8));
	}
	
	public List<User> followedUsers(String username) throws NotFoundException, ParsingException, HttpFetchException {
		return userParser.parseUserProfiles(httpFetcher.fetchContent(urlBuilder.userFollowedUsers(username), UTF_8));
	}
	
	public List<Noticeboard> followedNoticeboards(String username) throws NotFoundException, ParsingException, HttpFetchException {
		return noticeboardParser.parseNoticeboards(httpFetcher.fetchContent(urlBuilder.userFollowedNoticeboards(username), UTF_8));
	}
	
	public List<Noticeboard> noticeboards(String username) throws NotFoundException, ParsingException, HttpFetchException {
		return noticeboardParser.parseNoticeboards(httpFetcher.fetchContent(urlBuilder.userNoticeboards(username), UTF_8));
	}
	
	public Noticeboard noticeBoard(String noticeboard) throws NotFoundException, ParsingException, HttpFetchException {
		return noticeboardParser.parseNoticeboardResult(httpFetcher.fetchContent(urlBuilder.noticeBoard(noticeboard), UTF_8));
	}
	
	public User verify() throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.GET, apiUrl + "/verify");	// TODO most be a POST
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return userParser.parseUserProfile(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Content postReport(String headline, Double latitude, Double longitude, String body, String link, MediaFile image, VideoAttachment video, String noticeboard) throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException, BadRequestException, N0ticeException {
		return postReport(headline, latitude, longitude, body, link, image, video, noticeboard, null);		
	}
	
	public Noticeboard createNoticeboard(String domain, String name, String description, boolean moderated, Date endDate, Set<MediaType> supportedMediaTypes, String group) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, ParsingException, IOException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/noticeboards/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		addEntityPartParameter(entity, "domain", domain);
		addEntityPartParameter(entity, "name", name);
		addEntityPartParameter(entity, "description", description);
		addEntityPartParameter(entity, "moderated", Boolean.toString(moderated));
	
		if (endDate != null) {
			addEntityPartParameter(entity, "endDate", ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(endDate)));
		}
		
		StringBuilder supportedMediaTypesValue = new StringBuilder();
		Iterator<MediaType> supportedMediaTypesIterator = supportedMediaTypes.iterator();
		while(supportedMediaTypesIterator.hasNext()) {
			supportedMediaTypesValue.append(supportedMediaTypesIterator.next());
			if (supportedMediaTypesIterator.hasNext()) {
				supportedMediaTypesValue.append(COMMA);
			}
		}
		addEntityPartParameter(entity, "supportedMediaTypes", supportedMediaTypesValue.toString());
		
		if (group != null) {
			addEntityPartParameter(entity, "group", group);
		}
		
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return noticeboardParser.parseNoticeboardResult(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public void closeNoticeboard(String domain) throws NotFoundException, AuthorisationException, BadRequestException, NotAllowedException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, urlBuilder.closeNoticeboard(domain));
		oauthSignRequest(request);
		
		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Group createGroup(String name) throws IOException, ParsingException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/groups/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		addEntityPartParameter(entity, "name", name);
		
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseGroupResult(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Content postReport(String headline, Double latitude, Double longitude, String body, String link, MediaFile image, VideoAttachment video, String noticeboard, DateTime date) throws ParsingException, AuthorisationException, IOException, NotAllowedException, NotFoundException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/report/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (headline != null) {
			entity.addPart("headline", new StringBody(headline, Charset.forName(UTF_8)));
		}
		if (noticeboard != null) {
			entity.addPart("noticeboard", new StringBody(noticeboard, Charset.forName(UTF_8)));
		}

		if (latitude != null && longitude != null) {
			entity.addPart("latitude", new StringBody(Double.toString(latitude), Charset.forName(UTF_8)));
			entity.addPart("longitude", new StringBody(Double.toString(longitude), Charset.forName(UTF_8)));
		}
		
		populateUpdateFields(body, link, image, video, entity);
		
		if (date != null) {
			entity.addPart("date", new StringBody(date.toString(ZULE_TIME_FORMAT), Charset.forName(UTF_8)));
		}
		
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}

	public ResultSet authedSearch(SearchQuery searchQuery) throws ParsingException, HttpFetchException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {				
		OAuthRequest request = new OAuthRequest(Verb.GET, searchUrlBuilder.toUrl(searchQuery));
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseSearchResults(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Content postEvent(String headline, double latitude,
			double longitude, String body, String link, MediaFile image, VideoAttachment video,
			String noticeboard, LocalDateTime startDate, LocalDateTime endDate, Reoccurence reoccurence, LocalDateTime reoccursTo)
			throws ParsingException, AuthorisationException, IOException,
			NotAllowedException, NotFoundException, BadRequestException, N0ticeException {		
		final OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/event/new");

		final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		addEntityPartParameter(entity, "headline", headline);
		addEntityPartParameter(entity, "noticeboard", noticeboard);
		addEntityPartParameter(entity, "latitude", Double.toString(latitude));
		addEntityPartParameter(entity, "longitude", Double.toString(longitude));
		populateUpdateFields(body, link, image, video, entity);
		
		addEntityPartParameter(entity, "startDate", startDate.toString(LOCAL_DATE_TIME_FORMAT));
		addEntityPartParameter(entity, "endDate", endDate.toString(LOCAL_DATE_TIME_FORMAT));
		if (reoccurence != null && reoccursTo != null) {
			addEntityPartParameter(entity, "reoccurs", reoccurence.toString());
			addEntityPartParameter(entity, "reoccursTo", reoccursTo.toString(LOCAL_DATE_TIME_FORMAT));
		}
		
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Update postReportUpdate(String reportId, String body, String link, MediaFile image, VideoAttachment video) throws IOException, AuthorisationException, NotFoundException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + reportId  + "/update/new");
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		populateUpdateFields(body, link, image, video, entity);

		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return searchParser.parseUpdate(response.getBody());
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean voteInteresting(String id) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/vote/interesting");	
		oauthSignRequest(request);
		
		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return true;
		}

		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean flagAsInappropriate(String id, ModerationComplaintType type, String notes, String email) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		final OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/flag");
		if (type != null) {
			addBodyParameter(request, "type", type.toString());
		}
		if (notes != null) {
			addBodyParameter(request, "notes", notes);
		}
		if (email != null) {
			addBodyParameter(request, "email", email);
		}
		oauthSignRequest(request);
		
		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean repost(String id, String noticeboard) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/repost");
		addBodyParameter(request, "noticeboard", noticeboard);
		oauthSignRequest(request);

		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return true;
		}

		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean approve(String id) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/approve");
		oauthSignRequest(request);

		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return true;
		}

		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean reject(String id) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id + "/reject");
		oauthSignRequest(request);

		final Response response = request.send();		
		if (response.getCode() == 200) {
	    	return true;
		}

		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public int interestingVotes(String id) throws NotFoundException, ParsingException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.GET, apiUrl + "/" + id + "/votes/interesting");	
			
		final Response response = request.send();
		
		if (response.getCode() == 200) {
			return searchParser.parseVotes(response.getBody());
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public List<String> notifications(String username) throws NotFoundException, ParsingException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {		
		OAuthRequest request = new OAuthRequest(Verb.GET, urlBuilder.userNotifications(username));
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
			return searchParser.parseNotifications(response.getBody());
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public Content updateReport(String id, String headline, String body) throws ParsingException, AuthorisationException, NotFoundException, NotAllowedException, BadRequestException, N0ticeException {	
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/" + id);	
		addBodyParameter(request, "headline", headline);
		oauthSignRequest(request);
		
		Response response = request.send();
		
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {
	    	return searchParser.parseReport(responseBody);
		}
	
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean followUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username + "/follow");	
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean unfollowUser(String username) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username + "/unfollow");	
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean followNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/noticeboard/" + noticeboard + "/follow");	
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean unfollowNoticeboard(String noticeboard) throws NotFoundException, AuthorisationException, NotAllowedException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/noticeboard/" + noticeboard + "/unfollow");	
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
	    	return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public NewUserResponse createUser(String consumerKey, String username, String password, String email) throws ParsingException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/new");		
		addBodyParameter(request, "consumerkey", consumerKey);
		addBodyParameter(request, "username", username);
		addBodyParameter(request, "password", password);
		addBodyParameter(request, "email", email);
		
		final Response response = request.send();

		final String repsonseBody = response.getBody();
		if (response.getCode() == 200) {		
			return new UserParser().parseNewUserResponse(repsonseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public AccessToken authUser(String consumerKey, String username, String password, String consumerSecret) throws ParsingException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, N0ticeException {
		log.info("Attempting to auth user: " + consumerKey + ", " + username + ", " + password + ", " + consumerSecret);
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/auth");
		addBodyParameter(request, "consumerkey", consumerKey);
		addBodyParameter(request, "username", username);
		addBodyParameter(request, "password", password);

		// Manually sign this request using the consumer secret rather than the access key/access secret.
		addBodyParameter(request, "oauth_signature_method", "HMAC-SHA1");
		addBodyParameter(request, "oauth_version", "1.0");
		addBodyParameter(request, "oauth_timestamp", Long.toString(DateTimeUtils.currentTimeMillis()));
		final String effectiveUrl = request.getCompleteUrl() + "?" + request.getBodyContents();
		addBodyParameter(request, "oauth_signature", sign(effectiveUrl, consumerSecret));
		
		final Response response = request.send();
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {		
			return new UserParser().parseAuthUserResponse(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public AccessToken authGuardianUser(String consumerKey, String token, String consumerSecret) throws ParsingException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, N0ticeException {
		log.info("Attempting to auth guardian user: " + consumerKey + ", " + token);
		final OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/auth");
		addBodyParameter(request, "consumerkey", consumerKey);
		addBodyParameter(request, "guardianToken", token);

		// Manually sign this request using the consumer secret rather than the access key/access secret.
		addBodyParameter(request, "oauth_signature_method", "HMAC-SHA1");
		addBodyParameter(request, "oauth_version", "1.0");
		addBodyParameter(request, "oauth_timestamp", Long.toString(DateTimeUtils.currentTimeMillis()));
		final String effectiveUrl = request.getCompleteUrl() + "?" + request.getBodyContents();
		addBodyParameter(request, "oauth_signature", sign(effectiveUrl, consumerSecret));
		
		final Response response = request.send();
		final String responseBody = response.getBody();
		if (response.getCode() == 200) {		
			return new UserParser().parseAuthUserResponse(responseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public User updateUserDetails(String username, String displayName, String bio, MediaFile image) throws ParsingException, IOException, NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.POST, apiUrl + "/user/" + username);		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (displayName != null) {
			entity.addPart("displayName", new StringBody(displayName, Charset.forName(UTF_8)));
		}
		if (bio != null) {
			entity.addPart("bio", new StringBody(bio, Charset.forName(UTF_8)));
		}
		if (image != null) {
			entity.addPart("image", new ByteArrayBody(image.getData(), image.getFilename()));
		}
		request.addHeader("Content-Type", entity.getContentType().getValue());
		request.addPayload(extractMultpartBytes(entity));
		oauthSignRequest(request);
		
		Response response = request.send();

		final String repsonseBody = response.getBody();
		if (response.getCode() == 200) {
			return new UserParser().parseUserProfile(repsonseBody);
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	public boolean deleteReport(String id) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		OAuthRequest request = new OAuthRequest(Verb.DELETE, apiUrl + "/" + id);	
		oauthSignRequest(request);
		
		final Response response = request.send();
		
		if (response.getCode() == 200) {
			return true;
		}
		
		handleExceptions(response);
		throw new N0ticeException(response.getBody());
	}
	
	private void populateUpdateFields(String body, String link,
			MediaFile image, VideoAttachment video, MultipartEntity entity)
			throws UnsupportedEncodingException {
		if (body != null) {
			entity.addPart("body", new StringBody(body, Charset.forName(UTF_8)));
		}
		if (link != null) {
			entity.addPart("link", new StringBody(link, Charset.forName(UTF_8)));
		}
		if (image != null) {
			entity.addPart("image", new ByteArrayBody(image.getData(), image.getFilename()));
		}
		if (video != null) {
			entity.addPart("video", new InputStreamBody(video.getData(), video.getFilename()));			
		}
	}
	
	private byte[] extractMultpartBytes(MultipartEntity entity) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		entity.writeTo(byteArrayOutputStream);			
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		return byteArray;
	}
	
	private void handleExceptions(Response response) throws NotFoundException, NotAllowedException, AuthorisationException, BadRequestException, N0ticeException {
		log.error("Exception during n0tice api call: " + response.getCode() + ", " + response.getBody());
		if (response.getCode() == 404) {
			throw new NotFoundException("Not found");
		}
		if (response.getCode() == 403) {
			throw new NotAllowedException();
		}		
		if (response.getCode() == 401) {
			throw new AuthorisationException(response.getBody());
		}
		if (response.getCode() == 400) {
			throw new BadRequestException(response.getBody());
		}		
	}
	
	private void addBodyParameter(OAuthRequest request, String parameter, String value) {
		if (value != null) {
			request.addBodyParameter(parameter, value);
		}
	}
	
	private void addEntityPartParameter(MultipartEntity entity, String parameter, String value) {
		if (value != null) {
			try {
				entity.addPart(parameter, new StringBody(value, Charset.forName(UTF_8)));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void oauthSignRequest(OAuthRequest request) throws MissingCredentialsExeception {
		if (scribeAccessToken != null) {
			service.signRequest(scribeAccessToken, request);
			return;
		}
		throw new MissingCredentialsExeception();
	}

	private String sign(String effectiveUrl, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
	    SecretKeySpec key = new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA1");
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(key);
	    byte[] bytes = mac.doFinal(effectiveUrl.getBytes(UTF_8));
	    return new String(Base64.encodeBase64(bytes)).replace("\r\n", "");		  
	}
	
}
