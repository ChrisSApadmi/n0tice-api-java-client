package com.n0tice.api.client.parsers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n0tice.api.client.model.Noticeboard;

public class NoticeboardParserTest {

	@Test
	public void canParseDetailsFromNoticeboard() throws Exception {
		NoticeboardParser noticeboardParser = new NoticeboardParser();
		final Noticeboard noticeboard = noticeboardParser.parseNoticeboardResult(ContentLoader.loadContent("noticeboard.json"));

		assertEquals("Street Art", noticeboard.getName());
		assertEquals("streetart", noticeboard.getDomain());
		assertEquals("spotting #streetart around the world", noticeboard.getDescription());
		
		assertNotNull(noticeboard.getBackground());
	}

}
