package com.n0tice.api.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Image;
import com.n0tice.api.client.model.User;

public class UserParser {
	
	private static final String USERNAME = "username";	
	private static final String DISPLAY_NAME = "displayName";
	private static final String BIO = "bio";
	private static final String PROFILE_IMAGE = "image";
	private static final String SMALL = "small";

	public User parseCreateUserResults(String json) throws ParsingException {
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
		
		return new User(userJSON.getString(USERNAME), displayName, bio, profileImage);
	}
}
