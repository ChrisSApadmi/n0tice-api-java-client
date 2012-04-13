package com.n0tice.api.client;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OauthTest {

	
	public static void main(String[] args) {
		OAuthService service = new ServiceBuilder().provider(N0ticeApiOauthApi.class)
        .apiKey("tonr-consumer-key")
        .apiSecret("SHHHHH!!!!!!!!!!")
        .build();
		
		Token requestToken = service.getRequestToken();
		System.out.println(requestToken);		

		System.out.println(service.getAuthorizationUrl(requestToken));
		
	    Scanner in = new Scanner(System.in);
	    Verifier verifier = new Verifier(in.nextLine());
	    System.out.println();
	    
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    System.out.println(accessToken);

	    System.out.println(accessToken.getToken());
	    System.out.println(accessToken.getSecret());
	}

}
