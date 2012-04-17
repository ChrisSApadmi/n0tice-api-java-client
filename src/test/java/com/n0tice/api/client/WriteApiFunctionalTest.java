package com.n0tice.api.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

public class WriteApiFunctionalTest {
	
	private static final String API_URL = "http://dev.n0ticeapis.com/1";
	
	private static final String CONSUMER_SECRET = "testsecret";
	private static final String CONSUMER_KEY = "testkey";
	private static final String ACCESS_TOKEN = "6a3663d1-24c2-407f-8898-0d0aeaff130d";
	private static final String ACCESS_SECRET = "IrEq7VyCY6FpCVtKKBvXSoWv/Lv8fvT0qq3EsNDnmdN3iLlnGbXgqTZGXU87fnBFQgRk7Z9weNcw9uUqd9+CgBpSjfrMWqwBmPU5X63YnVo=";
	
	private N0ticeApi api;

	@Before
	public void setup() {
		Token accessToken = new Token(ACCESS_TOKEN, ACCESS_SECRET);
		api = new N0ticeApi(API_URL, CONSUMER_KEY, CONSUMER_SECRET, accessToken);		
	}
	
	@Test
	public void canPostNewReport() {		
		final String result = api.postRepost("API test", 51.0, -0.3, "Blah blah");
		
		System.out.println(result);
		assertTrue(result.contains("\"headline\":\"API test\""));
	}
	
}
