package com.n0tice.api.client.parsers;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.parsers.SearchParser;


public class SearchParserTest {

	private SearchParser searchParser;
	
	@Test
	public void canParseStopBoardJsonIntoListOfArrivals() throws Exception {
		searchParser = new SearchParser();
		
		List<Content> parsedItems = searchParser.parse(ContentLoader.loadContent("latestItems.json"));
		
		assertEquals(20, parsedItems.size());
		Content firstItem = parsedItems.get(0);
		assertEquals("report/2443/hackney-shares-top-position-in-london-jobless-league", firstItem.getId());
		assertEquals("/report/2443", firstItem.getApiUrl());
		assertEquals("http://hackney.n0tice.com/report/2443/hackney-shares-top-position-in-london-jobless-league", firstItem.getWebUrl());
		assertEquals("report", firstItem.getType());
		assertEquals("Hackney shares top position in London jobless league", firstItem.getHeadline());
		assertEquals("London Borough of Hackney, London E8 1EA, UK", firstItem.getPlace());
		assertEquals("KeithMagnum", firstItem.getUser());
		assertEquals(51.545032, firstItem.getLatitude(), 0);
		assertEquals(-0.056434, firstItem.getLongitude(), 0);
	}
	
}
