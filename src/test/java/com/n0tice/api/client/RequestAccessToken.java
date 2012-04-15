package com.n0tice.api.client;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.n0tice.api.client.oauth.N0ticeOauthApi;

public class RequestAccessToken {
	
	public static void main(String[] args) {		
		N0ticeOauthApi api = new N0ticeOauthApi("http://dev.n0ticeapis.com/1");
		
		OAuthService service = new ServiceBuilder().provider(api)
        	.apiKey("testkey")
        	.apiSecret("testsecret")
        	.build();
		
		Token requestToken = service.getRequestToken();
		System.out.println("Got request token: " + requestToken);		
		System.out.println("Authorise this token browsing to this URL:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		
		System.out.println("Enter validation code from API:");	// TODO Doesn't seem to be strictly required
	    Scanner in = new Scanner(System.in);
	    Verifier verifier = new Verifier(in.nextLine());
	    System.out.println();
	    
	    System.out.println("Exchanging authorised request token for access token");
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    System.out.println("Obtained access token: " + accessToken);
	    
	    System.out.println(accessToken.getToken());
	    System.out.println(accessToken.getSecret());
	}

}
