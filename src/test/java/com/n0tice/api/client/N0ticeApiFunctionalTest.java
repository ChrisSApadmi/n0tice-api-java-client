package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.Place;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.model.Tag;

public class N0ticeApiFunctionalTest {
	
	private static final String EVENT = "event";
	private static final double TWICKENHAM_LATITUDE = 51.446144;
	private static final double TWICKENHAM_LONGITUDE = -0.329719;
	private static final String TAG = "report/tags/hackney";
	private static final String ANOTHER_TAG = "reports/tags/crime";
	private static final String API_URL_ENV_PROP_KEY = "n0ticeapiurl";
	private static final String LIVE_API_URL = "http://n0ticeapis.com/1";
	private static final String USER = "tonytw1";
	private static final String NOTICE_BOARD = "hackney";
	private static final String TYPE = "offer";
	
	private N0ticeApi api;
	
	@Before
	public void setup() {		
		final String apiUrl = System.getenv(API_URL_ENV_PROP_KEY) != null ? System.getenv(API_URL_ENV_PROP_KEY) : LIVE_API_URL;
		api = new N0ticeApi("http://dev.n0ticeapis.com/1");
	}

	@Test
	public void canLoadLatestItems() throws Exception {		
		assertEquals(20, api.latest().getContent().size());		
	}
	
	@Test
	public void canLimitNumberOfResults() throws Exception {
		assertEquals(3, api.search(new SearchQuery().limit(3)).getContent().size());
	}
	
	@Test
	public void searchResultsShowsTotalMatchesCount() throws Exception {
		assertTrue(api.latest().getTotalMatches() > 1000);		
	}
	
	@Test
	public void searchResultsShowsCorrectStartIndexForNoPagination() throws Exception {
		assertEquals(0, api.latest().getStartIndex());
	}
	
	@Test
	public void searchResultsShowsCorrectStartIndexAsTheUserPaginations() throws Exception {
		assertEquals(40, api.page(3).latest().getStartIndex());
	}
	
	@Test
	public void searchResultsShouldHaveTypeSet() throws Exception {		
		for (Content content : api.latest().getContent()) {
			assertNotNull(content.getType());
		}
	}
	
	@Test
	public void eventsSearchResultsShouldHaveStartDateFieldsSet() throws Exception {
		for (Content content : api.type(EVENT).getContent()) {
			assertNotNull(content.getStartDate());
			assertNotNull(content.getEndDate());
		}
	}
	
	@Test
	public void canLoadItemsNearNamedLocation() throws Exception {		
		ResultSet results = api.near("Twickenham");
		assertFalse(results.getContent().isEmpty());
		for (Content content : results.getContent()) {		
			assertTrue("Result with place '" + content.getPlace() + "' is further than expected from named location", isWithinAboutTenKilometesOf(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE, content.getPlace()));
		}
	}
	
	@Test
	public void canLoadItemsNearLatitideAndLongitude() throws Exception {		
		ResultSet results = api.near(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE);
		for (Content content : results.getContent()) {		
			assertTrue("Result with place '" + content.getPlace() + "' is further than expected from named location", isWithinAboutTenKilometesOf(TWICKENHAM_LATITUDE, TWICKENHAM_LONGITUDE, content.getPlace()));
		}
	}

	@Test
	public void canLoadItemsForSpecificUser() throws Exception {		
		ResultSet results = api.user(USER);
		assertFalse(results.getContent().isEmpty());
		for (Content result : results.getContent()) {
			assertEquals("Result of search restricted by user contained a result with an unexpected user: " + result.toString(), USER, result.getUser().getUsername());
		}
	}
	
	@Test
	public void canRestrictSearchResultByContentType() throws Exception {
		ResultSet results = api.type(TYPE);
		assertEquals(20, results.getContent().size());
		for (Content result : results.getContent()) {
			assertEquals("Result of search restricted by content type contained an unexpected result: " + result.toString(), TYPE, result.getType());
		}
	}
	
	@Test
	public void canRestrictSearchResultByNoticeboard() throws Exception {
		ResultSet results = api.noticeboard(NOTICE_BOARD);
		assertEquals(11, results.getContent().size());
		for (Content result : results.getContent()) {
			assertEquals("Result of search restricted by notice board contained an unexpected result: " + result.toString(), NOTICE_BOARD, result.getNoticeBoard());
		}
	}
	
	@Test
	public void canRestrictSearchToSingleTag() throws Exception {
		ResultSet results = api.tag(TAG);
		assertTrue(results.getContent().size() > 0);
		for (Content result : results.getContent()) {
			assertTrue("Result of search restricted by tag contained an unexpected result: " + result.getTags().toString(), result.getTags().contains(new Tag(TAG)));
		}
	}
	
	@Test
	public void queriesWithMultipleTagsShouldReturnRecordsMatchingEthierTag() throws Exception {
		ResultSet results = api.search(new SearchQuery().tag(TAG).tag(ANOTHER_TAG));
		assertTrue(results.getContent().size() > 0);
		for (Content result : results.getContent()) {
			final boolean resultContainsOneOfTheExpectedTags = result.getTags().contains(new Tag(TAG)) || result.getTags().contains(new Tag(ANOTHER_TAG));
			assertTrue("Result of search restricted by tags contained an unexpected result: " + result.getTags().toString(), resultContainsOneOfTheExpectedTags);
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
		final String userProfileSmallImageUrl = content.getUser().getProfileImage().getSmall();
		assertTrue(userProfileSmallImageUrl.startsWith("http://"));
		assertTrue(userProfileSmallImageUrl.endsWith("/images/profile/small/b71d44947e8aeaa3.jpg"));
	}

	@Test
	public void singleEventsShouldShowFullyQualifiedProfileImageForPostingUser() throws Exception {
		Content content = api.get("event/555");
		assertTrue(content.getUser().getProfileImage().getSmall().startsWith("http://"));
		assertTrue(content.getUser().getProfileImage().getSmall().endsWith("/images/profile/small/827ef32cee7ecff6.jpg"));
	}
	
	@Test
	public void singleReportsShouldShowTagsWithCorrectTagIdFields() throws Exception {
		Content content = api.get("report/2527");
		assertFalse(content.getTags().isEmpty());
		assertEquals("report/tags/hackney", content.getTags().get(0).getId());
	}
	
	@Test
	public void singleEventsShouldShowTagsWithCorrectTagIdFields() throws Exception {
		Content content = api.get("event/551");
		assertFalse(content.getTags().isEmpty());
		assertEquals("event/tags/real-ale", content.getTags().get(1).getId());
	}
	
	@Test
	public void singleOffersShouldShowTagsWithCorrectTagIdFields() throws Exception {
		Content content = api.get("offer/459");
		assertFalse(content.getTags().isEmpty());
		assertEquals("offer/tags/armchair", content.getTags().get(0).getId());
	}
	
	private boolean isWithinAboutTenKilometesOf(double latitude, double longitude, Place place) {
		final int radius = 10;
		final double woobleInDegrees = radius * 0.01;		
		return place.getLatitude() > latitude - woobleInDegrees && place.getLatitude() < latitude + woobleInDegrees 
			&& place.getLongitude() > longitude - woobleInDegrees && place.getLongitude() < longitude + woobleInDegrees;
	}
	
}
