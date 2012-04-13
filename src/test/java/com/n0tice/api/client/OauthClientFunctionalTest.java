package com.n0tice.api.client;

import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class OauthClientFunctionalTest {
	
	@Test
	public void canObtainRequestToken() {
		OAuthService service = new ServiceBuilder().provider(TwitterAPI.class)
        .apiKey("")
        .apiSecret("")
        .build();
		
		Token requestToken = service.getRequestToken();
		System.out.println(requestToken);		
	}

}
