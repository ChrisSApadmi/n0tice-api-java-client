package com.n0tice.api.client;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class N0ticeApiOauthApi extends DefaultApi10a {

	private static final String API_URL = "http://localhost:8080/api-0.0.1-SNAPSHOT";
	private static final String AUTHORIZE_URL = API_URL + "/oauth/authorize?requestToken=%s";
	
	@Override
	public String getAccessTokenEndpoint() {
		return API_URL + "/oauth/access_token";
	}

	@Override
	public String getRequestTokenEndpoint() {
		return API_URL + "/oauth/request_token";
	}
	
	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

}
