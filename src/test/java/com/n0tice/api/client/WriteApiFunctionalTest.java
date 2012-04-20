package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

import com.n0tice.api.client.exceptions.AuthorisationException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.User;

public class WriteApiFunctionalTest {
	
	private static final String API_URL = "http://dev.n0ticeapis.com/1";
	
	private static final String CONSUMER_SECRET = "testsecret";
	private static final String CONSUMER_KEY = "testkey";
	private static final String ACCESS_TOKEN = "2807e4a2-ed65-43a9-8ba5-fefd8b22499b";
	private static final String ACCESS_SECRET = "JWYMQ8o6LZsKgK43/xqFpX6I2TCW9uP6D6yu3lAy0XDjzjb8/bOOvDJhh8nBl80tW1ubPSyWMZoQ+Wn2A3t+W6Xru5/7xNucf1qsEdNjSaw=";
	
	private N0ticeApi api;

	@Before
	public void setup() {
		Token accessToken = new Token(ACCESS_TOKEN, ACCESS_SECRET);
		api = new N0ticeApi(API_URL, CONSUMER_KEY, CONSUMER_SECRET, accessToken);		
	}
	
	@Test
	public void canCreateNewUser() throws Exception {		
		final String username = createNewTestUsername();
		System.out.println("Creating user: " + username);
		
		final User newUser = api.createUser(username, "testpassword", username + "@localhost");
		
		System.out.println(newUser);
		assertEquals(username, newUser.getUsername());
		assertEquals(username, newUser.getDisplayName());
	}
	
	@Test
	public void canUpdateUserDetails() throws Exception {
		final String newDisplayName = createNewTestUsername().toUpperCase();
		
		final User updatedUser = api.updateUserDetails("tonytw1", newDisplayName, "Just a test user");

		assertEquals(newDisplayName, updatedUser.getDisplayName());		
	}

	private String createNewTestUsername() {
		final String username = "testuser" + DateTime.now().getSecondOfDay();
		return username;
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
	
	@Test
	public void canDeleteReport() throws Exception {
		final Content report = api.postRepost("API test", 51.0, -0.3, "Blah blah");		
		
		final boolean result = api.deleteReport(report.getId());

		assertTrue(result);		
		assertNull(api.get(report.getId()));
	}
	
	@Test(expected = AuthorisationException.class)
	public void should401IfAnInvalidOauthTokenIsPresented() throws Exception {
		api = new N0ticeApi(API_URL, CONSUMER_KEY, CONSUMER_SECRET, new Token(ACCESS_TOKEN, "Meh"));
		
		api.postRepost("API test", 51.0, -0.3, "Blah blah");
	}
	
}
