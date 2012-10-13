package com.n0tice.api.client.urls;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.urls.UrlBuilder;

public class UrlBuilderTest {

	private static final String API_PREFIX = "http://n0ticeapis.com/1";
	private UrlBuilder urlBuilder;
	
	@Before
	public void setup() {
		urlBuilder = new UrlBuilder(API_PREFIX);
	}
	
	@Test
	public void canConstructUrlFromContentId() throws Exception {
		assertEquals("http://n0ticeapis.com/1/report/123", urlBuilder.get("report/123"));
	}
	
}
