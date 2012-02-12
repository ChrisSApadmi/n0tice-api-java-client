package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.Content;

public class N0ticeApiFunctionalTest {
	
	private static final String TAG = "reports/tag/hackney";
	private static final String API_URL_ENV_PROP_KEY = "n0ticeapiurl";
	private static final String LIVE_API_URL = "http://n0ticeapis.com/1";
	private static final String CONTENT_TYPE = "offer";
	private static final String USER = "mattmcalister";
	private static final String NOTICE_BOARD = "northerner";
	private static final String TYPE = "offer";
	
	private N0ticeApi api;
	
	@Before
	public void setup() {		
		final String apiUrl = System.getenv(API_URL_ENV_PROP_KEY) != null ? System.getenv(API_URL_ENV_PROP_KEY) : LIVE_API_URL;
		System.out.println("Api url is: " + apiUrl);
		api = new N0ticeApi(apiUrl);
	}

	@Test
	public void canLoadLatestItems() throws Exception {		
		assertEquals(20, api.latest().size());		
	}
	
	@Test
	public void searchResultsShouldHaveTypeSet() throws Exception {		
		for (Content content : api.latest()) {
			assertNotNull(content.getType());
		}
	}
	
	@Test
	public void canLoadItemsNearLocation() throws Exception {		
		assertEquals(20, api.near("London").size());		
	}
	
	@Test
	public void canLoadItemsForSpecificUser() throws Exception {		
		List<Content> results = api.user(USER);
		assertEquals(20, results.size());
		for (Content result : results) {
			assertEquals("Result of search restricted by user contained a result with an unexpected user: " + result.toString(), USER, result.getUser().getUsername());
		}
	}
	
	@Test
	public void canRestrictSearchResultByContentType() throws Exception {
		List<Content> results = api.type(CONTENT_TYPE);
		assertEquals(20, results.size());
		for (Content result : results) {
			assertEquals("Result of search restricted by content type contained an unexpected result: " + result.toString(), CONTENT_TYPE, result.getType());
		}
	}
	
	@Test
	public void canRestrictSearchResultByNoticeboard() throws Exception {
		List<Content> results = api.noticeboard(NOTICE_BOARD);
		assertEquals(20, results.size());
		for (Content result : results) {
			assertEquals("Result of search restricted by notice board contained an unexpected result: " + result.toString(), NOTICE_BOARD, result.getNoticeBoard());
		}
	}
	
	@Test
	public void canRestrictSearchToSingleTag() throws Exception {
		List<Content> results = api.tag(TAG);
		assertTrue(results.size() > 0);
		for (Content result : results) {
			assertTrue("Result of search restricted by tag contained an unexpected result: " + result.toString(), result.getTags().contains(TAG));
		}
	}
	
	@Test
	public void canLoadSingleReport() throws Exception {
		assertEquals("Finsbury Park Road. Neighbour's car broken into.", api.get("report/1054").getHeadline());
	}
	
	@Test
	public void singleReportsShouldShowId() throws Exception {
		assertEquals("report/1054", api.get("report/1054").getId());
	}
	
	@Test
	public void singleReportsShouldShowType() throws Exception {
		assertEquals("report", api.get("report/1054").getType());
	}
	
	@Test
	public void singleReportsShouldShowUsernameAndDisplayNameOfThePostingUser() throws Exception {
		Content content = api.get("report/1054");
		assertEquals("demwell", content.getUser().getUsername());
		assertEquals("Rachael Demwell", content.getUser().getDisplayName());
	}
	
	@Test
	public void singleReportsShouldShowFullyQualifiedProfileImageForPostingUser() throws Exception {
		Content content = api.get("report/1054");
		assertTrue(content.getUser().getProfileImage().startsWith("http://"));
		assertTrue(content.getUser().getProfileImage().endsWith("/images/profile/small/c1890f2a09cdba36.jpg"));
	}
	
}
