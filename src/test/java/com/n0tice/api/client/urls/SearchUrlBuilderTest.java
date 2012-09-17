package com.n0tice.api.client.urls;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class SearchUrlBuilderTest {

	private static final String API_URL = "http://api.local";

	private SearchUrlBuilder builder;

	@Before
	public void setup() {
		builder = new SearchUrlBuilder(API_URL);		
	}
	
	@Test
	public void searchAllUrl() throws Exception {
		assertEquals("http://api.local/search", builder.toUrl());			
	}
	
	@Test
	public void canLimitNumberOfResults() throws Exception {
		builder.limit(3);
		assertEquals("http://api.local/search?limit=3", builder.toUrl());
	}
	
	@Test
	public void canPaginate() throws Exception {
		builder.page(2);
		assertEquals("http://api.local/search?page=2", builder.toUrl());
	}
	
	@Test
	public void canMakeMultiTagSearches() throws Exception {
		builder.tags(Arrays.asList("atag", "anothertag"));
		assertEquals("http://api.local/search?tags=atag,anothertag", builder.toUrl());
	}
	
	@Test
	public void canLimitSearchToUser() throws Exception {
		builder.user("auser");
		assertEquals("http://api.local/search?user=auser", builder.toUrl());
	}
	
	@Test
	public void canBuildMultifieldQueries() throws Exception {
		builder.type("event").noticeBoard("formby");
		assertEquals("http://api.local/search?type=event&noticeboard=formby", builder.toUrl());
	}
	
	@Test
	public void parametersAreCorrectlyEncoded() throws Exception {
		builder.location("Eel Pie island");
		assertEquals("http://api.local/search?location=Eel+Pie+island", builder.toUrl());
	}
	
	@Test
	public void hashTagsInSearchQueriesAreCorrectlyEncoded() throws Exception {
		builder.q("#litloc");
		assertEquals("http://api.local/search?q=%23litloc", builder.toUrl());
	}
	
}
