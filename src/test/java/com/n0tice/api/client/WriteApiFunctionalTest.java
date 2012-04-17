package com.n0tice.api.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.oauth.N0ticeOauthApi;

public class WriteApiFunctionalTest {
	
	private static final String API_URL = "http://dev.n0ticeapis.com/1";
	private static final String TOKEN = "6a3663d1-24c2-407f-8898-0d0aeaff130d";
	private static final String SECRET = "IrEq7VyCY6FpCVtKKBvXSoWv/Lv8fvT0qq3EsNDnmdN3iLlnGbXgqTZGXU87fnBFQgRk7Z9weNcw9uUqd9+CgBpSjfrMWqwBmPU5X63YnVo=";
	
	private OAuthService service;

	@Before
	public void setup() {
		N0ticeOauthApi api = new N0ticeOauthApi(API_URL);
		service = new ServiceBuilder().provider(api)
				.apiKey("testkey")
				.apiSecret("testsecret")
				.build();
	}
	
	@Test
	public void canPostNewReportAuthenticatingViaOauth() {
		Token accessToken = new Token(TOKEN, SECRET);
		OAuthRequest request = new OAuthRequest(Verb.POST, API_URL + "/report/new");		
		request.addBodyParameter("headline", "API test");
		request.addBodyParameter("latitude", "51.0");
		request.addBodyParameter("longitude", "-0.3");
		request.addBodyParameter("body", "Blah blah");
		service.signRequest(accessToken, request);
	    
		Response response = request.send();
	    
	    final String responseBody = response.getBody();
	    System.out.println(responseBody);
		assertTrue(responseBody.contains("\"headline\":\"API test\""));
	}
	
}
