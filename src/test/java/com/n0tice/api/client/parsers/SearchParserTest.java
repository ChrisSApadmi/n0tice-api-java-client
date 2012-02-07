package com.n0tice.api.client.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.Content;

public class SearchParserTest {

	private SearchParser searchParser;
	
	@Before
	public void setup() {
		searchParser = new SearchParser();
	}
	
	@Test
	public void canParseContentItemsFromSearchResults() throws Exception {		
		List<Content> parsedItems = searchParser.parseSearchResults(ContentLoader.loadContent("latestItems.json"));
		
		assertEquals(20, parsedItems.size());
		Content firstItem = parsedItems.get(0);
		assertEquals("report/2443/hackney-shares-top-position-in-london-jobless-league", firstItem.getId());
		assertEquals("/report/2443", firstItem.getApiUrl());
		assertEquals("http://hackney.n0tice.com/report/2443/hackney-shares-top-position-in-london-jobless-league", firstItem.getWebUrl());
		assertEquals("report", firstItem.getType());
		assertEquals("Hackney shares top position in London jobless league", firstItem.getHeadline());
		assertEquals("London Borough of Hackney, London E8 1EA, UK", firstItem.getPlace());
		assertEquals("KeithMagnum", firstItem.getUser());
		assertEquals("hackney", firstItem.getNoticeBoard());
		assertEquals(51.545032, firstItem.getLatitude(), 0);
		assertEquals(-0.056434, firstItem.getLongitude(), 0);		
		assertEquals(new DateTime("2012-01-28T10:44:39Z"), new DateTime(firstItem.getCreated()));
		assertEquals(new DateTime("2012-01-28T10:44:39Z"), new DateTime(firstItem.getModified()));
		assertEquals(5, firstItem.getTags().size());
		assertEquals("reports/tag/hackney", firstItem.getTags().get(0));
	}
	
	@Test
	public void canParseSingleReportFromReportDetailsJson() throws Exception {
		Content report = searchParser.parseReport(ContentLoader.loadContent("reportWithUpdates.json"));
		
		assertEquals("2276", report.getId());			// TODO format is inconsistent with search
		assertEquals(null, report.getApiUrl());			// TODO not setting the api url is inconsistent with search
		assertEquals("http://n0tice.com/report/2276/graffiti-by-anonymous-in-brighton", report.getWebUrl());
		assertEquals("report", report.getType());
		assertEquals("Graffiti by Anonymous in Brighton", report.getHeadline());
		assertEquals("Western Rd, Brighton and Hove, The City of Brighton and Hove BN1, UK", report.getPlace());
		assertEquals("Luke", report.getUser());
		assertEquals(50.825080, report.getLatitude(), 0);
		assertEquals(-0.154179, report.getLongitude(), 0);
		assertEquals(new DateTime("2012-01-16T14:49:08Z"), new DateTime(report.getCreated()));
		assertEquals(new DateTime("2012-01-16T14:49:08Z"), new DateTime(report.getModified()));
		assertNotNull(report.getTags());	// TODO shows that reports endpoint does not include tags
	}
	
}
