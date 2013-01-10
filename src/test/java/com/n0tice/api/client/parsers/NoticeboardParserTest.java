package com.n0tice.api.client.parsers;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.n0tice.api.client.model.MediaType;
import com.n0tice.api.client.model.Noticeboard;

public class NoticeboardParserTest {

	@Test
	public void canParseDetailsFromNoticeboard() throws Exception {
		NoticeboardParser noticeboardParser = new NoticeboardParser();
		final Noticeboard noticeboard = noticeboardParser.parseNoticeboardResult(new JSONObject(ContentLoader.loadContent("noticeboard.json")));
		
		assertEquals("Test", noticeboard.getName());
		assertEquals("test", noticeboard.getDomain());
		assertEquals("Test board", noticeboard.getDescription());
		
		assertEquals("http://n0tice-devstatic.s3.amazonaws.com/images/noticeboards/backgrounds/large/41717efdefc22bd4.jpg", noticeboard.getBackground().getLarge());
		assertEquals(2, noticeboard.getSupportedMediaTypes().size());
		assertTrue(noticeboard.getSupportedMediaTypes().contains(MediaType.TEXT));
		assertTrue(noticeboard.getSupportedMediaTypes().contains(MediaType.IMAGE));

	}

}
