package com.n0tice.api.client.urls;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UrlBuilderTest {

	private static final String API_PREFIX = "http://n0ticeapis.com/2";
	private UrlBuilder urlBuilder;
	
	@Before
	public void setup() {
		urlBuilder = new UrlBuilder(API_PREFIX);
	}
	
	@Test
	public void canConstructUrlFromContentId() throws Exception {
		assertEquals("http://n0ticeapis.com/2/report/123", urlBuilder.get("report/123"));
	}
	
	@Test
	public void canComposeCloseNoticeboardUrl() throws Exception {
		assertEquals("http://n0ticeapis.com/2/noticeboard/someboard/close", urlBuilder.closeNoticeboard("someboard"));
	}
	
}
