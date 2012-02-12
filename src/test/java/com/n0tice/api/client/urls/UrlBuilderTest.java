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
	public void canConstructUrlForLatestItems() {				
		assertEquals("http://n0ticeapis.com/1/search", urlBuilder.latest());
	}
	
	@Test
	public void canConstructUrlForItemsNearNamedLocation() {				
		assertEquals("http://n0ticeapis.com/1/search?location=London", urlBuilder.near("London"));
	}
	
	@Test
	public void canConstructUrlForItemsNearPointLocation() {				
		assertEquals("http://n0ticeapis.com/1/search?latitude=51.4472&longitude=-0.3298", urlBuilder.near(51.4472, -0.3298));
	}
	
	@Test
	public void canConstructUrlForUsesLatestContent() throws Exception {
		assertEquals("http://n0ticeapis.com/1/search?user=auser", urlBuilder.user("auser"));
	}
	
	@Test
	public void canConstructUrlForSpecificContentType() throws Exception {
		assertEquals("http://n0ticeapis.com/1/search?type=offer", urlBuilder.type("offer"));
	}
	
	@Test
	public void canConstructUrlForSpecificNoticeboard() throws Exception {
		assertEquals("http://n0ticeapis.com/1/search?noticeboard=aboard", urlBuilder.noticeboard("aboard"));
	}
	
	@Test
	public void canConstructUrlForSpecificTag() throws Exception {
		assertEquals("http://n0ticeapis.com/1/search?tags=reports%2Ftag%2Fhackney", urlBuilder.tag("reports/tag/hackney"));
	}
	
	@Test
	public void canConstructUrlFromContentId() throws Exception {
		assertEquals("http://n0ticeapis.com/1/report/123", urlBuilder.get("report/123"));
	}
	
}
