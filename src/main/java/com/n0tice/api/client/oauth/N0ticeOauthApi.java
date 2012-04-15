package com.n0tice.api.client.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class N0ticeOauthApi extends DefaultApi10a {

	private final String apiUrl;
	
	public N0ticeOauthApi(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	@Override
	public String getRequestTokenEndpoint() {
		return apiUrl + "/oauth/request_token";
	}
	
	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(apiUrl + "/oauth/confirm_access?oauth_token=%s", requestToken.getToken());
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return apiUrl + "/oauth/access_token";
	}
	
}
