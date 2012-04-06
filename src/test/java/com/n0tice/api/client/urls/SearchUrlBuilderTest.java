package com.n0tice.api.client.urls;

import static org.junit.Assert.*;

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
	
}
