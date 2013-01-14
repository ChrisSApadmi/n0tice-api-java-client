package com.n0tice.api.client.parsers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.ResultSet;

public class SearchParserTest {

	private SearchParser searchParser;
	
	@Before
	public void setup() {
		searchParser = new SearchParser();
	}
	
	@Test
	public void canParseContentItemsFromSearchResults() throws Exception {		
		ResultSet results = searchParser.parseSearchResults(ContentLoader.loadContent("latestItems.json"));
		assertEquals(4052, results.getTotalMatches());
		
		assertEquals(20, results.getContent().size());
		Content firstItem = results.getContent().get(0);
		assertEquals("report/3626", firstItem.getId());
		assertEquals("http://n0ticeapis.com/1/report/3626", firstItem.getApiUrl());
		assertEquals("http://n0tice.com/report/3626/disassemblog---after-bella-taking-things-apart-httpafterbellablogspotcouk201204taking-things-aparthtml", firstItem.getWebUrl());
		assertEquals("report", firstItem.getType());
		assertEquals("DisassemBlog - After Bella: Taking Things Apart http://afterbella.blogspot.co.uk/2012/04/taking-things-apart.html", firstItem.getHeadline());
		assertEquals("Bath, Bath and North East Somerset, UK", firstItem.getPlace().getName());
		assertEquals("marcuslynch", firstItem.getUser().getUsername());
		assertEquals("n0tice", firstItem.getNoticeboard());
		assertEquals(51.375801, firstItem.getPlace().getLatitude(), 0);
		assertEquals(-2.359904, firstItem.getPlace().getLongitude(), 0);		
		assertEquals(new DateTime("2012-04-07T17:55:37Z", DateTimeZone.UTC), new DateTime(firstItem.getCreated()));
		assertEquals(new DateTime("2012-04-07T17:55:37Z", DateTimeZone.UTC), new DateTime(firstItem.getModified()));
		assertEquals(3, firstItem.getTags().size());
		assertEquals("report/tags/bath", firstItem.getTags().get(0).getId());
	}
	
	@Test
	public void canParseSingleReportFromReportDetailsJson() throws Exception {
		Content report = searchParser.parseReport(ContentLoader.loadContent("reportWithUpdates.json"));
		
		assertEquals("report/2276", report.getId());
		assertEquals("http://n0ticeapis.com/1/report/2276", report.getApiUrl());
		assertEquals("http://n0tice.com/report/2276/graffiti-by-anonymous-in-brighton", report.getWebUrl());
		assertEquals("report", report.getType());
		assertEquals("Graffiti by Anonymous in Brighton", report.getHeadline());
		assertEquals("Luke", report.getUser().getUsername());
		assertEquals("Western Rd, Brighton and Hove, The City of Brighton and Hove BN1, UK", report.getPlace().getName());
		assertEquals(50.825080, report.getPlace().getLatitude(), 0);
		assertEquals(-0.154179, report.getPlace().getLongitude(), 0);
		assertEquals(new DateTime("2012-01-16T14:49:08Z", DateTimeZone.UTC), new DateTime(report.getCreated()));
		assertEquals(new DateTime("2012-01-16T14:49:08Z", DateTimeZone.UTC), new DateTime(report.getModified()));
	}
	
	@Test
	public void canParseReportWithVideoUpdate() throws Exception {
		Content report = searchParser.parseReport(ContentLoader.loadContent("reportWithVideoUpdate.json"));
	}
	
}
