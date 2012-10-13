package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.SearchQuery;
import com.n0tice.api.client.parsers.SearchParser;
import com.n0tice.api.client.parsers.UserParser;
import com.n0tice.api.client.urls.SearchUrlBuilder;
import com.n0tice.api.client.urls.UrlBuilder;
import com.n0tice.api.client.util.HttpFetcher;

public class N0ticeApiTest {
	
	private static final String REPORT_ID = "/report/123";
	private static final String REPORT_API_URL = "http://n0ticeapi.../report/123";
	private static final String REPORT_JSON = "{report json}";
	
	private static final String SEARCH_URL = "http://n0ticeapi.../search";
	private static final String SEARCH_RESULTS_JSON = "{some json}";
	
	@Mock UrlBuilder urlBuilder;
	@Mock SearchUrlBuilder searchUrlBuilder;
	@Mock HttpFetcher httpFetcher;	
	@Mock SearchParser searchParser;
	@Mock UserParser userParser;
	
	@Mock private SearchQuery searchQuery;
	@Mock ResultSet content;
	@Mock Content report;
	
	private N0ticeApi api;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		api = new N0ticeApi("http://n0ticeapi...");
		
		ReflectionTestUtils.setField(api, "urlBuilder", urlBuilder, UrlBuilder.class);
		ReflectionTestUtils.setField(api, "searchUrlBuilder", searchUrlBuilder, SearchUrlBuilder.class);
		ReflectionTestUtils.setField(api, "httpFetcher", httpFetcher, HttpFetcher.class);
		ReflectionTestUtils.setField(api, "searchParser", searchParser, SearchParser.class);
		ReflectionTestUtils.setField(api, "userParser", userParser, UserParser.class);		 
	}
	
	@Test
	public void canSearchForContent() throws Exception {		
		when(searchUrlBuilder.toUrl(searchQuery)).thenReturn(SEARCH_URL);
		when(httpFetcher.fetchContent(SEARCH_URL, "UTF-8")).thenReturn(SEARCH_RESULTS_JSON);
		when(searchParser.parseSearchResults(SEARCH_RESULTS_JSON)).thenReturn(content);
		
		final ResultSet returnedItems = api.search(searchQuery);
		
		assertEquals(content, returnedItems);		
	}
	
	@Test
	public void canFetchReportDetails() throws Exception {
		when(urlBuilder.get(REPORT_ID)).thenReturn(REPORT_API_URL);
		when(httpFetcher.fetchContent(REPORT_API_URL, "UTF-8")).thenReturn(REPORT_JSON);
		when(searchParser.parseReport(REPORT_JSON)).thenReturn(report);
		
		Content returnedReport = api.get(REPORT_ID);
		
		assertEquals(report, returnedReport);
	}
	
	@Test(expected = HttpFetchException.class)
	public void shouldThrowInformativeExceptionIfHttpFetchFails() throws Exception {
		when(urlBuilder.get("report/123")).thenReturn(REPORT_API_URL);
		when(httpFetcher.fetchContent(REPORT_API_URL, "UTF-8")).thenThrow(new HttpFetchException());
		
		api.get("report/123");
	}
	
	@Test(expected = ParsingException.class)
	public void shouldThrowInformativeExceptionIfParsingFails() throws Exception {
		when(urlBuilder.get("report/123")).thenReturn(REPORT_API_URL);
		when(httpFetcher.fetchContent(REPORT_API_URL, "UTF-8")).thenReturn(REPORT_JSON);
		when(searchParser.parseReport(REPORT_JSON)).thenThrow(new ParsingException());
		
		api.get("report/123");
	}
	
}
