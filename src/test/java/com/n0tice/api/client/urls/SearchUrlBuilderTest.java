package com.n0tice.api.client.urls;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SearchUrlBuilderTest {

	private static final String API_URL = "http://api.local";

	@Test
	public void searchAllUrl() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);		
		assertEquals("http://api.local/search", builder.toUrl());			
	}
	
	@Test
	public void canLimitNumberOfResults() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);
		builder.limit(3);
		assertEquals("http://api.local/search?limit=3", builder.toUrl());
	}
	
	@Test
	public void canPaginate() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);
		builder.page(2);
		assertEquals("http://api.local/search?page=2", builder.toUrl());
	}
	
	@Test
	public void canMakeMultiTagSearches() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);		
		builder.tags(Arrays.asList("atag", "anothertag"));
		assertEquals("http://api.local/search?tags=atag,anothertag", builder.toUrl());
	}
	
	@Test
	public void canBuildMultifieldQueries() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);		
		builder.type("event").noticeBoard("formby");
		assertEquals("http://api.local/search?type=event&noticeboard=formby", builder.toUrl());
	}
	
	@Test
	public void parametersAreCorrectlyEncoded() throws Exception {
		SearchUrlBuilder builder = new SearchUrlBuilder(API_URL);		
		builder.location("Eel Pie island");
		assertEquals("http://api.local/search?location=Eel+Pie+island", builder.toUrl());
	}
	
}
