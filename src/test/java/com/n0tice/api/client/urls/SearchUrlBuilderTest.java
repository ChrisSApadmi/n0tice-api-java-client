package com.n0tice.api.client.urls;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.model.SearchQuery;

public class SearchUrlBuilderTest {

	private static final String API_URL = "http://api.local";
	
	private SearchUrlBuilder builder;

	@Before
	public void setup() {
		builder = new SearchUrlBuilder(API_URL);		
	}
	
	@Test
	public void searchAllUrl() throws Exception {
		assertEquals("http://api.local/search", builder.toUrl(new SearchQuery()));
	}
	
	@Test
	public void canLimitNumberOfResults() throws Exception {
		assertEquals("http://api.local/search?limit=3", builder.toUrl(new SearchQuery().limit(3)));
	}
	
	@Test
	public void canPaginate() throws Exception {
		assertEquals("http://api.local/search?page=2", builder.toUrl(new SearchQuery().page(2)));
	}
	
	@Test
	public void canConstructUrlForSpecificTag() throws Exception {
		assertEquals("http://api.local/search?tags=reports%2Ftag%2Fhackney", builder.toUrl(new SearchQuery().tag("reports/tag/hackney")));
	}
	
	@Test
	public void canMakeMultiTagSearches() throws Exception {
		assertEquals("http://api.local/search?tags=atag%2Canothertag", builder.toUrl(new SearchQuery().tag("atag").tag("anothertag")));
	}
	
	@Test
	public void canLimitSearchToUser() throws Exception {
		assertEquals("http://api.local/search?user=auser", builder.toUrl(new SearchQuery().user("auser")));
	}
	
	@Test
	public void canConstructUrlForSpecificNoticeboard() throws Exception {
		assertEquals("http://api.local/search?noticeboard=aboard", builder.toUrl(new SearchQuery().noticeBoard("aboard")));
	}
	
	@Test
	public void canBuildMultifieldQueries() throws Exception {
		assertEquals("http://api.local/search?type=event&noticeboard=formby", builder.toUrl(new SearchQuery().type("event").noticeBoard("formby")));
	}
	
	@Test
	public void parametersAreCorrectlyEncoded() throws Exception {
		assertEquals("http://api.local/search?location=Eel+Pie+island", builder.toUrl(new SearchQuery().location("Eel Pie island")));
	}
	
	@Test
	public void canConstructUrlForItemsNearPointLocation() {				
		assertEquals("http://api.local/search?latitude=51.4472&longitude=-0.3298", builder.toUrl(new SearchQuery().latitude(51.4472).longitude(-0.3298)));
	}
	
	@Test
	public void canSpecifyRadiusOfLocationSearch() {
		assertEquals("http://api.local/search?latitude=51.4472&longitude=-0.3298&radius=2.5",
				builder.toUrl(new SearchQuery().latitude(51.4472).longitude(-0.3298).radius(2.5)));
	}

	@Test
	public void hashTagsInSearchQueriesAreCorrectlyEncoded() throws Exception {
		assertEquals("http://api.local/search?q=%23litloc", builder.toUrl(new SearchQuery().q("#litloc")));
	}
	
}
