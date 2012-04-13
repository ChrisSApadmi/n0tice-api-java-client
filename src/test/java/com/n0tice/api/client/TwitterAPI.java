package com.n0tice.api.client;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class TwitterAPI extends DefaultApi10a {
	
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";

	@Override
	public String getAccessTokenEndpoint()
	{
	return "https://api.twitter.com/oauth/access_token";
	}

	@Override
	public String getRequestTokenEndpoint()
	{
	return "https://api.twitter.com/oauth/request_token";
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

}
