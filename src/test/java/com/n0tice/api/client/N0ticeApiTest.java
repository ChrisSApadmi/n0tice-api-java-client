package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.n0tice.api.client.N0ticeApi;
import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApiTest {
	
	private static final String REPORT_ID = "/report/123";
	private static final String LOCATION_NAME = "London";
	private static final String USER_NAME = "User";
	private static final String LATEST_ITEMS_URL = "http://n0ticeapi.../someuri";
	private static final String REPORT_API_URL = "http://n0ticeapi.../report/123";
	private static final String LATEST_ITEMS_JSON = "{some json}";
	private static final String REPORT_JSON = "{report json}";
	private static final String CONTENT_TYPE = "offer";
	private static final String NOTICE_BOARD = "streetart";
	private static final String TAG = "reports/tag/hackney";
	private static final String TYPE = "offer";
	
	@Mock UrlBuilder urlBuilder;
	@Mock HttpFetcher httpFetcher;	
	@Mock SearchParser searchParser;
	
	@Mock List<Content> latestItems;
	@Mock Content report;
	
	private N0ticeApi api;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		api = new N0ticeApi(urlBuilder, httpFetcher, searchParser);
	}
	
	@Test
	public void canFetchLatestItems() throws Exception {		
		when(urlBuilder.latest()).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.latest();
		
		assertEquals(latestItems, returnedItems);		
	}
	
	@Test
	public void canFetchReportDetails() throws Exception {
		when(urlBuilder.get(REPORT_ID)).thenReturn(REPORT_API_URL);
		when(httpFetcher.fetchContent(REPORT_API_URL, "UTF-8")).thenReturn(REPORT_JSON);
		when(searchParser.parseReport(REPORT_JSON)).thenReturn(report);
		
		Content returnedReport = api.get(REPORT_ID);
		
		assertEquals(report, returnedReport);
	}
	
	@Test
	public void canFetchLatestItemsNearNamedLocation() throws Exception {		
		when(urlBuilder.near(LOCATION_NAME)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.near(LOCATION_NAME);
		
		assertEquals(latestItems, returnedItems);		
	}
	
	@Test
	public void canFetchLatestItemsForUser() throws Exception {		
		when(urlBuilder.user(USER_NAME)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.user(USER_NAME);
		
		assertEquals(latestItems, returnedItems);		
	}
	
	@Test
	public void canFetchLatestItemsForContentType() throws Exception {		
		when(urlBuilder.type(CONTENT_TYPE)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.type(CONTENT_TYPE);
		
		assertEquals(latestItems, returnedItems);	
	}
	
	@Test
	public void canRestrictSearchToSpecificNoticeboard() throws Exception {
		when(urlBuilder.noticeboard(NOTICE_BOARD)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.noticeboard(NOTICE_BOARD);
		
		assertEquals(latestItems, returnedItems);	
	}
	
	@Test
	public void canRestrictSearchToSpecificTag() throws Exception {
		when(urlBuilder.tag(TAG)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.tag(TAG);
		
		assertEquals(latestItems, returnedItems);	
	}
	
	@Test
	public void canRestrictSearchToSpecificType() throws Exception {
		when(urlBuilder.type(TYPE)).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenReturn(latestItems);
		
		List<Content> returnedItems = api.type(TYPE);
		
		assertEquals(latestItems, returnedItems);	
	}
	
	@Test(expected = HttpFetchException.class)
	public void shouldThrowInformativeExceptionIfHttpFetchFails() throws Exception {
		when(urlBuilder.latest()).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenThrow(new HttpFetchException());
		
		api.latest();
	}
	
	@Test(expected = ParsingException.class)
	public void shouldThrowInformativeExceptionIfParsingFails() throws Exception {
		when(urlBuilder.latest()).thenReturn(LATEST_ITEMS_URL);
		when(httpFetcher.fetchContent(LATEST_ITEMS_URL, "UTF-8")).thenReturn(LATEST_ITEMS_JSON);
		when(searchParser.parseSearchResults(LATEST_ITEMS_JSON)).thenThrow(new ParsingException());
		
		api.latest();
	}
	
}
