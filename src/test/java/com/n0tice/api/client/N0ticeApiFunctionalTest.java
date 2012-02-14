package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.Place;

public class N0ticeApiFunctionalTest {
	
	private static final double TWICKENHAM_LATITUDE = 51.446144;
	private static final double TWICKENHAM_LONGITUDE = -0.329719;
	private static final String TAG = "reports/tag/hackney";
	private static final String API_URL_ENV_PROP_KEY = "n0ticeapiurl";
	private static final String LIVE_API_URL = "http://n0ticeapis.com/1";
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
	
	public void canLimitNumberOfResults() throws Exception {
		fail();
	}
	
	@Test
	public void searchResultsShowsTotalMatchesCount() throws Exception {
		fail();
	}
	
	@Test
	public void searchResultsShowsCorrectStartIndex() throws Exception {
		fail();
	}
	
	@Test
	public void searchResultsShouldHaveTypeSet() throws Exception {		
		for (Content content : api.latest()) {
			assertNotNull(content.getType());
		}
	}
	
	@Test
	public void canLoadItemsNearNamedLocation() throws Exception {		
		List<Content> results = api.near("Twickenham");
		assertFalse(results.isEmpty());
		for (Content content : results) {		
			assertTrue("Result with place '" + content.getPlace() + "' is further than expected from named location", isWithinAboutTenKilometesOf(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE, content.getPlace()));
		}
	}
	
	@Test
	public void canLoadItemsNearLatitideAndLongitude() throws Exception {		
		List<Content> results = api.near(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE);
		for (Content content : results) {		
			assertTrue("Result with place '" + content.getPlace() + "' is further than expected from named location", isWithinAboutTenKilometesOf(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE, content.getPlace()));
		}
	}

	@Test
	public void canLoadItemsForSpecificUser() throws Exception {		
		List<Content> results = api.user(USER);
		assertFalse(results.isEmpty());
		for (Content result : results) {
			assertEquals("Result of search restricted by user contained a result with an unexpected user: " + result.toString(), USER, result.getUser().getUsername());
		}
	}
	
	@Test
	public void canRestrictSearchResultByContentType() throws Exception {
		List<Content> results = api.type(TYPE);
		assertEquals(20, results.size());
		for (Content result : results) {
			assertEquals("Result of search restricted by content type contained an unexpected result: " + result.toString(), TYPE, result.getType());
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
	
	@Test
	public void singleReportsShouldShowTagsWithCorrectTagIdFields() throws Exception {
		Content content = api.get("report/2527");
		assertFalse(content.getTags().isEmpty());
		assertEquals("reports/tag/hackney", content.getTags().get(0).getId());
	}
	
	private boolean isWithinAboutTenKilometesOf(double latitude, double longitude, Place place) {
		final int radius = 10;
		final double woobleInDegrees = radius * 0.01;		
		return place.getLatitude() > latitude - woobleInDegrees && place.getLatitude() < latitude + woobleInDegrees 
			&& place.getLongitude() > longitude - woobleInDegrees && place.getLongitude() < longitude + woobleInDegrees;
	}
	
}
