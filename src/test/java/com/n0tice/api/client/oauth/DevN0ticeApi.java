package com.n0tice.api.client.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class DevN0ticeApi extends DefaultApi10a {

	private static final String API_URL = "http://dev.n0ticeapis.com/1";
	private static final String AUTHORIZE_URL = API_URL + "/oauth/confirm_access?oauth_token=%s&callback=%s";
	
	@Override
	public String getRequestTokenEndpoint() {
		return API_URL + "/oauth/request_token";
	}
	
	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken(), "http://localhost/meh");
	}

	@Override
	public String getAccessTokenEndpoint() {
		return API_URL + "/oauth/access_token";
	}
}
