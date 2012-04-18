package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.model.Content;

public class WriteApiFunctionalTest {
	
	private static final String API_URL = "http://dev.n0ticeapis.com/1";
	
	private static final String CONSUMER_SECRET = "testsecret";
	private static final String CONSUMER_KEY = "testkey";
	private static final String ACCESS_TOKEN = "9f6be38d-835f-4824-ae94-b85220f76614";
	private static final String ACCESS_SECRET = "oeL3AzzvWM1mPFkdOzSR6b0VypCGrrs8K6vpxMykJoW9kA2Qd8vDGZ7u27Qjb4OO1mc0KiN8yJPkokP7WG48PXMPhDApL65bIHFq1+QhZVQ=";
	
	private N0ticeApi api;

	@Before
	public void setup() {
		Token accessToken = new Token(ACCESS_TOKEN, ACCESS_SECRET);
		api = new N0ticeApi(API_URL, CONSUMER_KEY, CONSUMER_SECRET, accessToken);		
	}
	
	@Test
	public void canPostNewReport() throws Exception {		
		final Content result = api.postRepost("API test", 51.0, -0.3, "Blah blah");		
		System.out.println(result);
		assertEquals("API test", result.getHeadline());
		
		final Content reloadedReport = api.get(result.getId());
		assertEquals("API test", reloadedReport.getHeadline());
		assertEquals("tonytw1", reloadedReport.getUser().getUsername());
	}
	
	@Test
	public void canUpdateReport() throws Exception {		
		final Content report = api.postRepost("API test", 51.0, -0.3, "Blah blah");		
		assertEquals("API test", report.getHeadline());
		
		api.updateReport(report.getId(), "New headline", "New Body");
		
		final Content reloadedReport = api.get(report.getId());
		assertEquals("New headline", reloadedReport.getHeadline());
	}
	
	@Test(expected = AuthorisationException.class)
	public void should401IfAnInvalidOauthTokenIsPresented() throws Exception {
		api = new N0ticeApi(API_URL, CONSUMER_KEY, CONSUMER_SECRET, new Token(ACCESS_TOKEN, "Meh"));
		
		api.postRepost("API test", 51.0, -0.3, "Blah blah");
	}
	
}
