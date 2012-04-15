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
	private static final String TOKEN = "d84a489c-4add-4f5b-85c6-569ccda4d546";
	private static final String SECRET = "mJrk/V1jRggrKMNLMpMYuAirDdtVbu2VdTOHlCeVIWCm/t7MBacCepB2zCmf+GVkZhLjQ9JNYelBne4aZjfzPYAWzhagd5ZtMnbUiSCfFCo=";
	
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
