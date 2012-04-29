package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Image;
import com.n0tice.api.client.model.User;

public class UserParser {
	
	private static final String DOMAIN = "domain";
	private static final String FOLLOWING = "following";
	private static final String NOTICEBOARDS = "noticeboards";
	private static final String USERNAME = "username";	
	private static final String DISPLAY_NAME = "displayName";
	private static final String BIO = "bio";
	private static final String PROFILE_IMAGE = "image";
	private static final String SMALL = "small";

	public User parseUserProfile(String json) throws ParsingException {
		try {
			JSONObject userJson = new JSONObject(json);
			return jsonToUser(userJson);
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}		
	}
	
	public User jsonToUser(JSONObject userJSON) throws JSONException {
		String displayName = null;
		String bio = null;
		Image profileImage = null;
		if (userJSON.has(DISPLAY_NAME)) {
			displayName = userJSON.getString(DISPLAY_NAME);
		}
		if (userJSON.has(BIO)) {
			bio = userJSON.getString(BIO);
		}
		if (userJSON.has(PROFILE_IMAGE)) {
			JSONObject imageJSON = userJSON.getJSONObject(PROFILE_IMAGE);
			profileImage = new Image(imageJSON.getString(SMALL));
		}
		
		List<String> noticeboards = new ArrayList<String>();
		if (userJSON.has(NOTICEBOARDS)) {
			JSONArray noticeboardsJSON = userJSON.getJSONArray(NOTICEBOARDS);
			parseNoticeboards(noticeboards, noticeboardsJSON);			
		}
		
		List<String> followedNoticeboards = new ArrayList<String>();
		if (userJSON.has(FOLLOWING)) {
			JSONObject followsJSON = userJSON.getJSONObject(FOLLOWING);
			if (followsJSON.has(NOTICEBOARDS)) {
				JSONArray noticeboardsJSON = followsJSON.getJSONArray(NOTICEBOARDS);
				parseNoticeboards(followedNoticeboards, noticeboardsJSON);			
			}
		}
		return new User(userJSON.getString(USERNAME), displayName, bio, profileImage, noticeboards, followedNoticeboards);
	}

	private void parseNoticeboards(List<String> noticeboards,
			JSONArray noticeboardsJSON) throws JSONException {
		for (int i = 0; i < noticeboardsJSON.length(); i++) {
			JSONObject jsonNoticeboard = noticeboardsJSON.getJSONObject(i);
			noticeboards.add(jsonNoticeboard.getString(DOMAIN));
		}
	}
	
}
