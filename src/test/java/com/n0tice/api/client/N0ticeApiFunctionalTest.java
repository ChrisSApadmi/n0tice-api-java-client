package com.n0tice.api.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.n0tice.api.client.N0ticeApi;


public class N0ticeApiFunctionalTest {
	
	private static final String LIVE_API_URL = "http://n0ticeapis.com/1";
	
	private N0ticeApi api;

	@Before
	public void setup() {		
		api = new N0ticeApi(LIVE_API_URL);
	}

	@Test
	public void canLoadLatestItems() throws Exception {		
		assertEquals(20, api.latest().size());		
	}
	
	@Test
	public void canLoadItemsNearLocation() throws Exception {		
		assertEquals(20, api.near("London").size());		
	}
	
	@Test
	public void canLoadItemsForUser() throws Exception {		
		assertEquals(20, api.user("mattmcalister").size());
	}
	
}
