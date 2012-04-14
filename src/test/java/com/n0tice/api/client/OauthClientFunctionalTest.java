package com.n0tice.api.client;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.oauth.DevN0ticeApi;

public class OauthClientFunctionalTest {
	
	private OAuthService service;

	@Before
	public void setup() {
		service = new ServiceBuilder().provider(DevN0ticeApi.class)
        .apiKey("tonr-consumer-key")
        .apiSecret("SHHHHH!!!!!!!!!!")
        .build();
	}
		
	@Test
	public void canAccessProtectedResourceWithAccessToken() {
		Token accessToken = new Token("8425805e-ce86-4c4a-a305-38eaaedd4308", "35LaPCy0bHK8ImAEUXmOgynW3jAfVXYnBH6u/UmNH7qH6pgsRSsyrmaYhllWnHRcg73zMRL9YlZ8yl2VeVlJm7jGNXL570q1P1cqeuctikA=");
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://localhost:8080/api-0.0.1-SNAPSHOT/event/551");
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    System.out.println(response.getBody());
	    
	}

}
